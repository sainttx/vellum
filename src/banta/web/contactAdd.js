
function contactAddReady() {
    $('.contactAdd-clickable').click(contactAddClick);
    $('#contactAdd-container').load('contactAdd.html', function() {
        $('#contactAdd-form').submit(contactAddSubmit);        
    });
}

function contactAddClick() {
    console.log("contactAddClick");
    $('.page-container').hide();
    $('#contactAdd-container').show();
}

function contactAddSubmit() {
    console.log("contactAddSubmit");

}