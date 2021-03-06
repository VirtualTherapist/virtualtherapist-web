package helpers;

import models.*;
import play.libs.Crypto;

/**
 * DatabaseFunctionsHelper, sets up the values in the database when needed.
 * All methods should be static. So they can be accessed from anywhere.
 */
public class DatabaseFunctionsHelper
{
    /**
     * addUserRole adds a regular user to the database.
     * @return  UserRole    The regular user role
     */
    public static UserRole addUserRole() {
        UserRole userRole   = new UserRole();
        userRole.level      = 1;
        userRole.name       = "Gebruiker";

        userRole.save();

        return userRole;
    }

    /**
     * Save admin role in the database
     * @return  UserRole    The admin user role
     */
    public static UserRole addAdminRole() {
        UserRole rol = new UserRole();
            rol.level = 10;
            rol.id= 2;
            rol.name  = "Super Admin";
        rol.save();

        return rol;
    }

    /**
     * Adds a user according to the parameters.
     * @param email         String      Email for the user
     * @param first_name    String      First name of the user
     * @param last_name     String      Last name of the user
     * @param password      String      Password of the user
     * @param role          UserRole    The user role which applies to the user
     * @return              User        The created user in the databse is returned.
     */
    public static User addUserWithInput(String email, String first_name, String last_name, String password, UserRole role) {
        User user       = new User();
        user.email      = email;
        user.first_name = first_name;
        user.last_name  = last_name;
        user.password   = Crypto.encryptAES(password);
        user.role       = role;
        user.save();

        return user;
    }

    /**
     * Adds user with predefined fields, and custom role
     * @param rol   UserRole    The user role which to add to the user.
     * @return      User        The created user
     */
    public static User addUser(UserRole rol) {
        User user = new User();
            user.email = "user@therapist.com";
            user.first_name = "User";
            user.last_name = "Name";
            user.password = Crypto.encryptAES("password");
            user.role = rol;
        user.save();

        return user;
    }

    /**
     * Deletes a user given the user object.
     * @param user  User    The user to delete
     */
    public static void deleteUser(User user) {
        user.delete();
    }

    /**
     * createAnser, creates a new entry in the answer table
     * @param answer    Answer  The answer which to add
     * @return answer   Answer  The answer which was created
     */
    public static Answer createAnswer(String answer) {
        Answer ans = new Answer();
        ans.answer = answer;
        ans.save();
        return ans;
    }

    /**
     * createQuestion, creates a new question entry in the questions table
     * @param question  Question    The question which is being added
     * @param answer    Answer      The answer which belongs to this question
     * @return question Question    The question which was created
     */
    public static Question createQuestion(String question, Answer answer, User user){
        Question qu = new Question();
        qu.question = question;
        qu.user = user;
        qu.answer   = answer;
        qu.save();

        return qu;
    }

    /**
     * deleteAnswer, deletes the specified answer from the database.
     * @param answer    Answer      The answer which is being deleted
     */
    public static void deleteAnswer(Answer answer) {
        answer.delete();
    }

    /**
     * deleteQuestion, deletes the specified question from the database.
     * @param question  Question    The question which is being deleted
     */
    public static void deleteQuestion(Question question) {
        question.delete();
    }

    /**
     * createQuestionKeyword, creates a question keyword entry in the database
     * @param question  Question        The question which is being linked to the keyword
     * @param kq        KeywordCategory The keyword category id which is being linked
     * @return QuestionKeyword          The question keyword object that was created
     */
    public static QuestionKeyword createQuestionKeyword(Question question, KeywordCategory kq) {
        QuestionKeyword qk = new QuestionKeyword();
        qk.keywordCategory = kq;
        qk.question = question;
        qk.save();

        return qk;
    }
    /**
     * createCategory, creates a new category entry in the category table
     * @param category  String      The name of the category
     * @return          Category    The created category as object
     */
    public static Category createCategory(String category){
        Category c = new Category();
        c.name  = category;
        c.save();

        return c;
    }

    /**
     * deleteCategory, deletes the category according the category
     * @param category Category     The category which needs to be deleted
     */
    public static void deleteCategory(Category category){
        category.delete();
    }

    /**
     * createKeyword, creates a new entry in the keyword table
     * @param keyword   String      The keyword which should be added
     * @return          Keyword     The keyword object which was saved
     */
    public static Keyword createKeyword(String keyword){
        Keyword k = new Keyword();
        k.keyword = keyword;
        k.save();

        return k;
    }

    /**
     * createKeywordCategory, creates a new entry in the keyword_cateogry table
     * @param k Keyword         The keyword which is being linked
     * @param c Category        The category which is being linked
     * @return  KeywordCategory The keywordcategory object which was saved
     */
    public static KeywordCategory createKeywordCategory(Keyword k, Category c){
        KeywordCategory keywordCategory = new KeywordCategory();
        keywordCategory.category    = c;
        keywordCategory.keyword     = k;
        keywordCategory.save();

        return keywordCategory;
    }

    /**
     * createUserQuestion, creates a new entry in the user question table
     * @param user  User     The user to link the question to
     * @param question Question    The question which is being asked by the user
     * @return  UserQuestion    The user question that was just added
     */
    public static UserQuestion createUserQuestion(User user, String question){
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.asked_question = question;
        userQuestion.user   = user;
        userQuestion.save();

        return userQuestion;
    }

    /**
     * createUserQuestionKeywordCategory, creates a new entry in the userQuestionKeyword table.
     * @param uq    UserQuestion        The question which was asked by the user
     * @param kc    KeywordCategory     The keyword category to which the question belongs to
     * @return      UserQuestionKeyword The user question keyword which was created
     */
    public static UserQuestionKeyword createUserQuestionKeywordCategory(UserQuestion uq, KeywordCategory kc){
        UserQuestionKeyword uqk = new UserQuestionKeyword();
        uqk.keywordCategory = kc;
        uqk.userquestion = uq;
        uqk.save();

        return uqk;
    }

    /**
     * deleteKeyword, removes the specified keyword from the table
     * @param keyword Keyword   The keyword which is being removed
     */
    public static void deleteKeyword(Keyword keyword) {
        keyword.delete();
    }

    /**
     * deleteKeywordCategory, removes the specified keyword category
     * @param kc    KeywordCategory     The keywordcategory which is being removed.
     */
    public static void deleteKeywordCategory(KeywordCategory kc){
        kc.delete();
    }

    /**
     * deleteQuestionKeyword, removes the questionkeyword entry.
     * @param questionKeyword   QuestionKeyword     The questionkeyword entry that needs to be deleted
     */
    public static void deleteQuestionKeyword(QuestionKeyword questionKeyword){
        questionKeyword.delete();
    }

    /**
     * deleteUserQuestion, removes the userQuestion entry in the database.
     */
    public static void deleteUserQuestion(UserQuestion userQuestion){
        userQuestion.delete();
    }
}
