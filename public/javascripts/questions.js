(function ($){
    $.fn.editable.defaults.mode = 'inline';

    $('.update-delete-question').each(function(key, value){
        $(value).editable();
    })
})(jQuery)