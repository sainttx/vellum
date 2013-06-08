
function contactEditReady() {
    $('.contactAdd-clickable').click(contactAddClick);
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
    state.contact = contact;
    console.log("contactEdit", contact);
    $('#contactEdit-legend').text('Edit contact');
    contactEditSet(contact);
    contactEditShow();
}

function contactAddClick() {
    state.contact = null;
    $('#contactEdit-legend').text('Add contact');
    contactEditClear();
    contactEditShow();
    contactEditFocus();
}

function contactEditShow() {
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
