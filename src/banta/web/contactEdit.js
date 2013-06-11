

var contactEditValidatorConfig = {
    rules: {
        name: {
            minlength: 2,
            required: true
        },
        email: {
            required: false,
            email: true
        },
        mobile: {
            minlength: 10,
            maxlength: 10,
            digits: true,
            required: false
        }
    },
    highlight: contactEditHighlight,
    success: contactEditSuccess
}

var contactEditErrorElement = null;

function contactEditHighlight(element) {
    console.log("contactEditHighlight", element);
    $(element).closest('.control-group').removeClass('success').addClass('error');
    contactEditButtons(false);
    $(element).focus();
    errorElement = element;
}

function contactEditSuccess(element) {
    console.log("contactEditSuccess");
    $(element).closest('.control-group').removeClass('error').addClass('success');
    contactEditButtons(true);
}

function contactEditButtons(ok) {
    if (ok) {
        $('#contactEdit-save').addClass('btn-primary');
        $('#contactEdit-cancel').removeClass('btn-primary');
    } else {
        $('#contactEdit-save').removeClass('btn-primary');
        $('#contactEdit-cancel').addClass('btn-primary');
    }
}

var contactEditValidator = null;

function contactEditReady(loaded) {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#contactEdit-container').load('contactEdit.html', function() {
        contactEditLoaded(loaded);
    });
}

function contactEditLoaded(loaded) {
    contactEditValidator = $('#contactEdit-form').validate(contactEditValidatorConfig);
    $('#contactEdit-save').click(contactEditSave);
    $('#contactEdit-cancel').click(contactEditCancel);
    $('#contactEdit-cancel').focus(contactEditCancelFocus);
    loaded();
}

function contactEditClickable() {
    return contactEditValidator !== null;
}

function contactEditCancelFocus(event) {
    if (contactEditValidator.valid()) {
        $('#contactEdit-save').focus();        
    }
}

function contactEdit(contact) {
    window.history.pushState(null, null, "/#contactEdit/" + contact.name.replace(/\s+/g, ''));
    state.contact = contact;
    console.log("contactEdit", contact);
    $('#title').text('Edit contact');
    $('#contactEdit-legend').text('Edit contact');
    contactEditClear();
    contactEditSet(contact);
    contactEditShow();
}

function contactAddClick() {
    window.history.pushState(null, null, "/#contactAdd");
    state.contact = null;
    $('#title').text('Add contact');
    $('#contactEdit-legend').text('Add contact');
    contactEditClear();    
    contactEditShow();
    contactEditFocus();
    return true;
}

function contactEditShow() {
    contactEditValidator.resetForm();
    $('#contactEdit-cancel').addClass('btn-primary');
    $('#contactEdit-save').removeClass('btn-primary');
    $('.page-container').hide();
    $('#contactEdit-container').show();
}

function contactEditSave(event) {
    console.log("contactEditSave");
    event.preventDefault();
    contactEditErrorElement = null;
    if ($('#contactEdit-form').valid()) {
        var contact = contactEditGet();
        contactsPut(contact);
        server.ajax({
            url: '/contactEdit',
            data: $('#contactEdit-form').serialize(),
            success: contactEditRes,
            error: contactEditError,
            memo: contact
        });
    } else {
        contactEditButtons(false);
    }
}

function contactEditRes(res) {
    console.log('contactEditRes');
    console.log(res);
    contactsClick();
}

function contactEditError() {
    console.log('contactEditError');
}

function contactEditCancel() {
    console.log("contactEditCancel");
    contactEditClear();
    contactsClick();
}

function contactEditClear() {
    console.log("contactEditClear", $('#contactEdit-form > fieldset > .control-group').length);
    contactEditValidator.resetForm();
    contactEditButtons(false);
    $('#contactEdit-form > fieldset > div.control-group').removeClass('error');
    $('#contactEdit-form > fieldset > div.control-group').removeClass('success');
    contactEditSet({
        name: '',
        mobile: '',
        email: ''
    });
}

function contactEditSet(o) {
    $('#contactEdit-name-input').val(o.name);
    $('#contactEdit-mobile-input').val(o.mobile);
    $('#contactEdit-email-input').val(o.email);
}

function parseName(text) {
    return text.replace(/[<>]/gi, ' ');
}

function parseMobile(text) {
    return text.replace(/[^ +0-9]/gi, '');
}

function parseEmail(text) {
    return text.replace(/[<>]/gi, '');
}

function contactEditGet() {
    return {
        name: parseName($('#contactEdit-name-input').val()),
        mobile: parseMobile($('#contactEdit-mobile-input').val()),
        email: parseEmail($('#contactEdit-email-input').val())
    };
}

function contactEditFocus() {
    $('#contactEdit-name-input').focus();
}
