
function contactAddReady() {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#contactAdd-container').load('contactAdd.html', function() {
        contactAddLoad();
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

function contactAddLoad() {    
    $('#contactAdd-save').click(contactAddSave);
    $('#contactAdd-cancel').click(contactAddCancel);
    $('#contactAdd-name-input').change('#contactAdd-save', changeButtonPrimary);
}

function contactAddClick() {
    console.log("contactAddClick");
    contactAddClear();
    $('.page-container').hide();
    $('#contactAdd-container').show();
    contactAddFocus();
}

function contactAddSave() {
    console.log("contactAddSave");
    event.preventDefault();
    var contact = contactAddGet();
    if (contact.name.length === 0) {
        return false;
    }
    server.ajax({
        url: '/contactAdd',
        data: $('#contactAdd-form').serialize(),
        success: contactAddRes,
        error: contactAddError,
        memo: contact
    });
    return false;
}

function contactAddRes(res) {
    console.log('contactAddRes');    
    console.log(res);
    contactsClick();
}

function contactAddError() {
    console.log('contactAddError');    
}

function contactAddCancel() {
    console.log("contactAddCancel");
    contactsClick();
}

function contactAddClear() {
    $('#contactAdd-save').removeClass('btn-primary');    
    contactAddSet({
        name: '', 
        mobile: '', 
        email: ''
    });
}

function contactAddSet(o) {
    $('#contactAdd-name-input').val(o.name);
    $('#contactAdd-mobile-input').val(o.mobile);
    $('#contactAdd-email-input').val(o.email);
}

function contactAddGet() {
    return {
        name: $('#contactAdd-name-input').val(),
        mobile: $('#contactAdd-mobile-input').val(),
        email: $('#contactAdd-email-input').val()
    };   
}

function contactAddFocus() {
    $('#contactAdd-name-input').focus();
}
