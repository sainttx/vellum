
function contactEditReady() {
    $('#contactEdit-container').load('contactEdit.html', function() {
        $('.contactEdit-clickable').click(contactEditClick);    
        $('#contactEdit-form').submit(contactEditSubmit);    
    });
}

function contactEditClick() {
    console.log('contactEditClick');
    console.log(orgMeta.contactEdit);
    $('.croc-info').hide();
    //buildInputs($('#contactEdit-fieldset'), '', orgMeta.contactEdit);
    $('#contactEdit-container').show();
}

function contactEditSet(org) {
    $('#contactEdit-orgId').val(org.orgId);
    $('#contactEdit-orgUrl').val(org.orgUrl);
    $('#contactEdit-orgCode').val(org.orgCode);
    $('#contactEdit-displayName').val(org.displayName);
    $('#contactEdit-region').val(org.region);
    $('#contactEdit-locality').val(org.locality);
    $('#contactEdit-country').val(org.country);
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

