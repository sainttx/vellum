
function contactsReady() {
    console.log("contactsReady");
    $('.contacts-clickable').click(contactsClick);
}

function contactsClick() {
    console.log("contactsClick");
    $('.page-container').hide();
    $('#contacts-container').show();
}
