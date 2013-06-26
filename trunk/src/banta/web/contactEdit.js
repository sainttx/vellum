

var contactEditValidatorConfig = {
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

function contactEditLoaded() {
    contactEditValidator = $('#contactEdit-form').validate(contactEditValidatorConfig);
    $('.contactNew-clickable').click(contactNewClick);
    $('#contactEdit-save').click(contactEditSave);
    $('#contactEdit-cancel').click(contactEditCancel);
    $('#contactEdit-cancel').focus(contactEditCancelFocus);
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
    state.contact = contact;
    $('.chat-clickable').addClass('btn-primary');
    $('.chat-clickable').show();
    console.log("contactEdit", contact);
    contactEditClear();
    contactEditSet(contact);
    $('#contactEdit-legend').text('Edit contact');
    showPage('Edit contact', 'contactEdit', 'contactEdit', contact.name);
}

function contactNewClick() {
    setPath('contactNew');
    state.contact = null;
    contactEditClear();    
    $('#contactEdit-legend').text('New contact');
    showPage('New contact', 'contactEdit', 'contactNew', null);
    contactEditFocus();
}

function contactEditSave(event) {
    console.log("contactEditSave");
    event.preventDefault();
    contactEditErrorElement = null;
    if ($('#contactEdit-form').valid()) {
        var contact = contactEditGet();
        console.log("contactEditSave", contact);
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
    b.contacts.click();
}

function contactEditError() {
    console.log('contactEditError');
}

function contactEditCancel() {
    console.log("contactEditCancel");
    contactEditClear();
    b.contacts.click();
}

function contactEditClear() {
    console.log("contactEditClear", $('#contactEdit-form > fieldset > .control-group').length);
    contactEditValidator.resetForm();
    $('#contactEdit-cancel').addClass('btn-primary');
    $('#contactEdit-save').removeClass('btn-primary');
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

function contactEditGet() {
    return {
        name: u.string.sanitize($('#contactEdit-name-input').val()),
        mobile: $('#contactEdit-mobile-input').val(),
        email: $('#contactEdit-email-input').val()
    };
}

function contactEditFocus() {
    $('#contactEdit-name-input').focus();
}
