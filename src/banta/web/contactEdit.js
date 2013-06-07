
function contactEditReady() {
    $('.contactEdit-clickable').click(contactEditClick);
    $('#contactEdit-container').load('contactEdit.html', function() {
        contactEditLoad();
    });
}

function changeButtonPrimary(event) {
    console.log('changeButtonPrimary', event.data);
    if (this.value.length > 0) {
        $(event.data).addClass('btn-primary');
    } else {
        $(event.data).removeClass('btn-primary');
    }
}

function contactEditLoad() {    
    $('#contactEdit-save').click(contactEditSave);
    $('#contactEdit-cancel').click(contactEditCancel);
    $('#contactEdit-name-input').change('#contactEdit-save', changeButtonPrimary);
}

function contactEdit(contact) {
    console.log("contactEdit", contact);
    contactEditSet(contact);
    contactEditShow();
}

function contactEditClick() {
    contactEditClear();
    contactEditShow();
}

function contactEditShow() {
    $('.page-container').hide();
    $('#contactEdit-container').show();
    contactEditFocus();
}

function contactEditSave() {
    console.log("contactEditSave");
    event.preventDefault();
    var contact = contactEditGet();
    if (contact.name.length === 0) {
        return false;
    }
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

function contactEditGet() {
    return {
        name: $('#contactEdit-name-input').val(),
        mobile: $('#contactEdit-mobile-input').val(),
        email: $('#contactEdit-email-input').val()
    };   
}

function contactEditFocus() {
    $('#contactEdit-name-input').focus();
}
