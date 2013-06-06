
function contactAddReady() {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#contactAdd-container').load('contactAdd.html', function() {
        $('#contactAdd-save').click(contactAddSave);
        $('#contactAdd-cancel').click(contactAddCancel);
        
    });
}

function contactAddClick() {
    console.log("contactAddClick");
    $('.page-container').hide();
    $('#contactAdd-container').show();
}

function contactAddSave() {
    console.log("contactAddSave");
    event.preventDefault();
    server.ajax({
        url: '/contactAdd',
        data: $('#contactAdd-form').serialize(),
        success: contactAddRes,
        error: contactAddError
    });
    return false;
}

function contactAddRes(res) {
    console.log('processEditOrg');    
    console.log(res);
}

function contactAddError() {
    console.log('errorEditOrg');    
}

function contactAddCancel() {
    console.log("contactAddCancel");
    contactsClick();
}

function contactAddSet(o) {
    $('#contactAdd > input[name=name]').val(o.name);
}
