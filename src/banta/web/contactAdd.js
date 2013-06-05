
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
}

function contactAddCancel() {
    console.log("contactAddCancel");
}