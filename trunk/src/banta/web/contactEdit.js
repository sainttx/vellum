
function contactEditReady() {
    $('#contactEdit-container').load('contactEdit.html', function() {
        $('.contactEdit-clickable').click(contactEditClick);    
        $('#contactEdit-form').submit(contactEditSubmit);    
    });
}

function contactEditClick() {
    console.log('contactEditClick');
    console.log(orgMeta.contactEdit);
    $('.page-container').hide();
    $('#contactEdit-container').show();
}

function contactEditSet(o) {
    $('#contactEdit-name').val(o.name);
    $('#contactEdit-mobile').val(o.mobile);
    $('#contactEdit-email').val(o.email);
}

function contactEditSubmit(event) {
    console.log('contactEditSubmit');    
    event.preventDefault();
    server.ajax({
        url: '/contactEdit',
        data: $('#contactEdit-form').serialize(),
        success: contactEditRes,
        error: contactEditError
    });
    return false;
}

function contactEditRes(res) {
    console.log('processEditOrg');    
    console.log(res);
}

function contactEditError() {
    console.log('errorEditOrg');    
}

