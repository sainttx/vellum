
function contactEditReady() {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#contactEdit-container').load('contactEdit.html', function() {
        contactEditLoad();
    });
}

function changeButtonPrimary(event) {
    console.log('changeButtonPrimary', event.data);
    if (this.value.length > 0) {
        $('#contactEdit-cancel').removeClass('btn-primary');
        $('#contactEdit-save').addClass('btn-primary');
    } else {
        $('#contactEdit-save').removeClass('btn-primary');
        $('#contactEdit-cancel').addClass('btn-primary');
    }
}

function contactEditLoad() {    
    $('#contactEdit-save').click(contactEditSave);
    $('#contactEdit-cancel').click(contactEditCancel);
    $('#contactEdit-name-input').change(changeButtonPrimary);
}

function contactEdit(contact) {
    state.contact = contact;
    console.log("contactEdit", contact);
    $('#title').text('Edit contact');    
    $('#contactEdit-legend').text('Edit contact');
    contactEditSet(contact);
    contactEditShow();
}

function contactAddClick() {
    state.contact = null;
    $('#title').text('Add contact');    
    $('#contactEdit-legend').text('Add contact');
    contactEditClear();
    contactEditShow();
    contactEditFocus();
}

function contactEditShow() {
    $('#contactEdit-cancel').addClass('btn-primary');
    $('#contactEdit-save').removeClass('btn-primary');
    $('.page-container').hide();
    $('#contactEdit-container').show();
}

function contactEditSave() {
    console.log("contactEditSave");
    event.preventDefault();
    var contact = contactEditGet();
    if (contact.name.length === 0) {
        return false;
    }    
    contactsPut(contact);
    server.ajax({
        url: '/contactEdit',
        data: $('#contactEdit-form').serialize(),
        success: contactEditRes,
        error: contactEditError,
        memo: contact
    });
    return false;
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
    contactsClick();
}

function contactEditClear() {
    $('#contactEdit-save').removeClass('btn-primary');    
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
    return text.replace(/[<>]/gi,' ');
}

function parseMobile(text) {
    return text.replace(/[^ +0-9]/gi,'');
}

function parseEmail(text) {
    return text.replace(/[<>]/gi,'');
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
