package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import filters.APIAuthHeaderFilter;
import com.wordnik.swagger.annotations.*;
import models.*;
import play.Logger;
import play.libs.F;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.mvc.With;
import utils.NLPUtil;
import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Controller.request;
import static play.mvc.Results.*;

/**
 * Created by Akatchi on 10-10-2014.
 */
@Api(value = "/api", description = "API operations", basePath = "http://localhost:9000/api")
public class APIController extends SwaggerBaseApiController {
    @ApiOperation(nickname = "ValidateLogin", value="ValidateLogin", notes = "Validates app login", response = User.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login valid"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "sha256(Email + password)", required = true, dataType = "string", paramType = "header")
    })
    @With(APIAuthHeaderFilter.class)
    public static Result validateLogin() {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

        if (user != null) {
            return ok(toJson(user));
        }
        return unauthorized();
    }

    @ApiOperation(nickname = "SetChatRating", value= "SetRatingOfChat", notes = "Rates a chat.", response = Integer.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat updated with rating"),
            @ApiResponse(code = 400, message = "Invalid or missing variables"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rating", value = "User rating of the chat", required = true, dataType = "int", paramType = "post"),
            @ApiImplicitParam(name = "chat_id", value = "Id of chat", required = true, dataType = "int", paramType = "post")
    })
    @With(APIAuthHeaderFilter.class)
    public static Result setChatRating() {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

        Map<String, String[]> postVariables = request().body().asFormUrlEncoded();

        int rating, chatId;
        try {
            // Retrieve 'rating' and 'chat_id' variables as integers.
            rating = Integer.parseInt(postVariables.get("rating")[0]);
            chatId = Integer.parseInt(postVariables.get("chat_id")[0]);
        } catch (NullPointerException e) {
            Logger.debug("Could rate chat. Chat id or rating is missing.");
            return badRequest("Variables 'rating' and/or 'chat_id' are missing.");
        }

        Chat chat = Ebean.find(Chat.class, chatId);
        
        // User can't rate chats from other users.
        if (!chat.user.equals(user)) {
            Logger.debug("Could rate chat. User is not chat owner."); 
            return unauthorized();
        }

        chat.rating = rating;
        chat.save();

        Logger.debug("Rated chat " + chatId + " with " + rating + " stars.");
        return ok();
    }

    @ApiOperation(nickname = "CreateChatWithContext", value="CreateChatWithContext", notes = "Creates a chat and adds te context", response = Integer.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Chat and context created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Missing mood variable")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mood", value = "User mood", required = true, dataType = "string", paramType = "post"),
            @ApiImplicitParam(name = "lat", value = "User location lat", required = false, dataType = "double", paramType = "post"),
            @ApiImplicitParam(name = "lng", value = "User location lng", required = false, dataType = "double", paramType = "post")
    })
    @With(APIAuthHeaderFilter.class)
    public static Result setChatContext() {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

        Map<String, String[]> postVariables = request().body().asFormUrlEncoded();
        if(postVariables == null || !postVariables.containsKey("mood"))
            return badRequest("Missing mood variable");

        String mood = postVariables.get("mood")[0];
        String lat = null;
        String lng = null;
        if(postVariables.containsKey("lat") && postVariables.containsKey("lng")) {
            lat = postVariables.get("lat")[0];
            lng = postVariables.get("lng")[0];
        }

        Chat chat = new Chat();
        chat.user = user;

        if (lat != null && lng != null) {
            chat.lat = Double.parseDouble(lat);
            chat.lng = Double.parseDouble(lng);
        } else {
            chat.lat = 0;
            chat.lng = 0;
        }

        chat.mood = mood;
        chat.save();

        if (chat != null) {
            return created(toJson(chat.id));
        }

        return internalServerError();
    }

    private static Map<String, String> parseJson(String s) {

        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(s, 
                    new TypeReference<HashMap<String,String>>(){});
            Logger.debug("Json: " + map);
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    @ApiOperation(nickname = "WebSocket", value="", notes = "Returns chat websocket", response = WebSocket.class, httpMethod = "GET")
    public static WebSocket<String> WebSocket()
    {
        return new WebSocket<String>()
        {
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<String> in, final WebSocket.Out<String> out) {
                // For each event received on the socket,
                in.onMessage(new F.Callback<String>() {
                    public void invoke(String event) {
                        // parse the json string from websocket into a map
                        Map<String, String> data = parseJson(event);
                        Logger.debug("incomming: "+event);
                        if(data.containsKey("token"))
                        { // check for the token
                            if(APIAuthHeaderFilter.authenticate(data.get("token")))
                            { // see if the token is valid.
                                if( data.containsKey("question") ) // see if there's a question to be checked
                                {
                                    String user_email   = data.get("email"); // fetch the user mail
                                    User user           = Ebean.find(User.class).where().eq("email", user_email).findUnique();
                                    Chat userChat       = Ebean.find(Chat.class, data.get("chatid")); // get the latest room
                                    String question     = data.get("question"); // fetch the question

                                    SortedMap<String, String>[] tokens  = NLPUtil.getInstance().tagMessage(question);
                                    List<Object> retrievedData          = storeChat(user, question, tokens); // store everything that's being said
                                    UserQuestion userQ                  = (UserQuestion) retrievedData.get(0);

                                    //Code om de beste question uit te kiezen
                                    //Hier moet straks het hele keyword zoeken gebeuren maar dit is evne voor he gemaakt gedaan
                                    Question bestQuestion      = null;
                                    int record                 = 9999;
                                    List<String> userQKeywords = new ArrayList<String>((List<String>)retrievedData.get(1));

                                    for(String item : userQKeywords)
                                    {
                                        Logger.debug("Questionkeyword: " + item);
                                    }

                                    Logger.debug("APIController: UserQuestion " + userQ.asked_question);

                                    for(Question q : Ebean.find(Question.class).findList())
                                    {

                                        Logger.debug(q.question);

                                        List<String> questionKeywords = new ArrayList<String>();
                                        for(QuestionKeyword keyword : Ebean.find(QuestionKeyword.class).where().eq("question", q).findList())
                                        {
                                            questionKeywords.add(keyword.keywordCategory.keyword.keyword);
                                        }

                                        int preCheckSize    = userQKeywords.size();
                                        Logger.debug("voor " + questionKeywords.size() + " || " + userQKeywords.size());
                                        userQKeywords.removeAll(questionKeywords);
                                        Logger.debug("na " + questionKeywords.size() + " || " + userQKeywords.size());
                                        int afterCheckSize  = userQKeywords.size();

                                        Logger.debug("Question: " + q.question + " hits met userquestion: " + question + " || " + userQKeywords.size());

                                        if( preCheckSize != afterCheckSize)//Dus geen hits gevonden
                                        {
                                            if(userQKeywords.size() < record)
                                            {
                                                record       = userQKeywords.size();
                                                bestQuestion = q;
                                                Logger.debug("Record: " + record);
                                            }
                                        }
                                    }


                                    //Code om als er geen questoin gevonden is in ieder geval een standaard antwoord terug te sturen
                                    String answerString;
                                    if(bestQuestion == null)    { answerString = "Geen antwoord gevonden"; }
                                    else                        { answerString = bestQuestion.answer.answer; }

                                    Answer bestAnswer = Ebean.find(Answer.class).where().eq("answer", answerString).findUnique();
                                    if( bestAnswer == null )
                                    {
                                        bestAnswer = new Answer();
                                            bestAnswer.answer = answerString;
                                        bestAnswer.save();
                                    }

                                    Logger.debug("Chat: "       + userChat.mood);
                                    Logger.debug("Bestanswer: " + bestAnswer.answer);
                                    Logger.debug("Question: "   + userQ.asked_question);

                                    ChatLine line  = new ChatLine();
                                    line.chat           = userChat;
                                    line.answer         = bestAnswer;
                                    line.userQuestion   = userQ;
                                    line.save();

                                    Logger.debug("Chatline: " + line.id);

                                    Logger.debug("Antwoord: " + bestAnswer.answer);

                                    //Het antwoord terugsturen naar de client
                                    out.write(bestAnswer.answer);
                                }
                            }
                        }
                    }
                });

                // When the socket is closed.
                in.onClose(new F.Callback0() {
                    public void invoke() {
                        Logger.debug("Disconnected");
                    }
                });
            }
        };
    }

    /**
     * @param user
     * @param question
     * @param keywords
     */
    private static ArrayList<Object> storeChat(User user, String question, SortedMap<String, String>[] keywords) {
        List<Object> toReturn = new ArrayList<Object>();
        // Store the userquestion
        UserQuestion q          = new UserQuestion();
        q.asked_question    = question;
        q.user              = user;
        q.save();

        toReturn.add(q);

        List<String> keywordsList = new ArrayList<String>();

        // store keywords
        for(SortedMap<String, String> map : keywords) {
            for(Map.Entry<String, String> entry : map.entrySet()) {
                Logger.debug("key: " + entry.getKey() + " Value: " + entry.getValue());

                Category cat = Ebean.find(Category.class).where().eq("name", entry.getValue()).findUnique();
                if (cat == null) {
                    cat = new Category();
                    cat.name = entry.getValue();
                    cat.save();
                }

                Logger.debug("Category: " + cat.name);

                Keyword keyword = Ebean.find(Keyword.class).where().eq("keyword", entry.getKey()).findUnique();
                if (keyword == null) {
                    keyword = new Keyword();
                    keyword.keyword = entry.getKey();
                    keyword.save();
                }

                Logger.debug("Keyword: " + keyword.keyword);

                KeywordCategory keyCat = Ebean.find(KeywordCategory.class).where().eq("keyword", keyword).eq("category", cat).findUnique();
                if (keyCat == null) {
                    keyCat = new KeywordCategory();
                    keyCat.keyword  = keyword;
                    keyCat.category = cat;
                    keyCat.save();
                }

                Logger.debug("Koppeling: " +  keyCat.id);

                UserQuestionKeyword link = new UserQuestionKeyword();
                link.userquestion       = q;
                link.keywordCategory    = keyCat;
                link.save();

                Logger.debug("Link: " + link.id);

                keywordsList.add(entry.getKey());
            }

            toReturn.add(keywordsList);
        }

        Logger.debug("Question: " + q.asked_question);

        return (ArrayList<Object>) toReturn;
    }
}
