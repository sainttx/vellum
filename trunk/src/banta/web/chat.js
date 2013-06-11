

var chatValidatorConfig = {
    rules: {
        name: {
            minlength: 2,
            required: true,
            sanitary: true
        },
        mobile: {
            minlength: 10,
            maxlength: 10,
            digits: true,
            required: false
        },
        email: {
            required: false,
            email: true
        }
    },
    highlight: chatHighlight,
    success: chatSuccess
}

var chatErrorElement = null;

function chatHighlight(element) {
    console.log("chatHighlight", element);
    $(element).closest('.control-group').removeClass('success').addClass('error');
    chatButtons(false);
    $(element).focus();
    errorElement = element;
}

function chatSuccess(element) {
    console.log("chatSuccess");
    $(element).closest('.control-group').removeClass('error').addClass('success');
    chatButtons(true);
}

function chatButtons(ok) {
    if (ok) {
        $('#chat-save').addClass('btn-primary');
        $('#chat-cancel').removeClass('btn-primary');
    } else {
        $('#chat-save').removeClass('btn-primary');
        $('#chat-cancel').addClass('btn-primary');
    }
}

var chatValidator = null;

function chatReady(loaded) {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#chat-container').load('chat.html', function() {
        chatLoaded(loaded);
    });
}

function chatLoaded(loaded) {
    chatValidator = $('#chat-form').validate(chatValidatorConfig);
    $('#chat-save').click(chatSave);
    $('#chat-cancel').click(chatCancel);
    $('#chat-cancel').focus(chatCancelFocus);
    loaded();
}

function chatClickable() {
    return chatValidator !== null;
}

function chatCancelFocus(event) {
    if (chatValidator.valid()) {
        $('#chat-save').focus();        
    }
}

function chat(contact) {
    window.history.pushState(null, null, "/#chat/" + contact.name.replace(/\s+/g, ''));
    state.contact = contact;
    console.log("chat", contact);
    $('#title').text('Edit contact');
    $('#chat-legend').text('Edit contact');
    chatClear();
    chatSet(contact);
    chatShow();
}

function contactAddClick() {
    window.history.pushState(null, null, "/#contactAdd");
    state.contact = null;
    $('#title').text('Add contact');
    $('#chat-legend').text('Add contact');
    chatClear();    
    chatShow();
    chatFocus();
    return true;
}

function chatShow() {
    chatValidator.resetForm();
    $('#chat-cancel').addClass('btn-primary');
    $('#chat-save').removeClass('btn-primary');
    $('.page-container').hide();
    $('#chat-container').show();
}

function chatSave(event) {
    console.log("chatSave");
    event.preventDefault();
    chatErrorElement = null;
    if ($('#chat-form').valid()) {
        var contact = chatGet();
        console.log("chatSave", contact);
        contactsPut(contact);
        server.ajax({
            url: '/chat',
            data: $('#chat-form').serialize(),
            success: chatRes,
            error: chatError,
            memo: contact
        });
    } else {
        chatButtons(false);
    }
}

function chatRes(res) {
    console.log('chatRes');
    console.log(res);
    contactsClick();
}

function chatError() {
    console.log('chatError');
}

function chatCancel() {
    console.log("chatCancel");
    chatClear();
    contactsClick();
}

function chatClear() {
    console.log("chatClear", $('#chat-form > fieldset > .control-group').length);
    chatValidator.resetForm();
    chatButtons(false);
    $('#chat-form > fieldset > div.control-group').removeClass('error');
    $('#chat-form > fieldset > div.control-group').removeClass('success');
    chatSet({
        name: '',
        mobile: '',
        email: ''
    });
}

function chatSet(o) {
    $('#chat-name-input').val(o.name);
    $('#chat-mobile-input').val(o.mobile);
    $('#chat-email-input').val(o.email);
}

function chatGet() {
    return {
        name: sanitize($('#chat-name-input').val()),
        mobile: $('#chat-mobile-input').val(),
        email: $('#chat-email-input').val()
    };
}

function chatFocus() {
    $('#chat-name-input').focus();
}
