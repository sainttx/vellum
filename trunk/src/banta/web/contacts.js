
function contactsArray(o) {
    return [o.name];
};

function contactsReady() {
    console.log("contactsReady");
    $('.contacts-clickable').click(contactsClick);
}

function contactsClick() {
    console.log("contactsClick");
    contactsBuild(server.login.contacts);
    $('.page-container').hide();
    $('#contacts-container').show();
}

function contactsSort(array) {
    array.sort(function(a, b) {
        if (a.name === b.name) {
            return 0;
        } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
            return 1;
        }
        return -1;
    });    
}

function contactsFind(array) {
}

function contactsBuild(array) {
    contactsSort(array);
    buildTable($('#contacts-tbody'), contactsArray, array);
    $("#contacts-tbody > tr").click(function() {
        contactsListRowClick($(this).children('td').first().text());
    });    
}

function contactsListRowClick(id) {
    var contact = contactsFind(id);
    contactEdit(contact);
}

