
function contactsReady() {
    console.log("contactsReady");
    $('.contacts-clickable').click(contactsClick);
}

function contactsClick() {
    console.log("contactsClick");
    buildContacts(server.loginRes.contacts);
    $('.page-container').hide();
    $('#contacts-container').show();
}

var contactsListHandler = {
    columnArray: function(o) {
        return [o];
    },
};

function buildContacts(contactList) {
    console.log('buildContacts');
    console.log(contactList);
    buildTable($('#contacts-tbody'), contactsListHandler, contactList);
    $("#contacts-tbody > tr").click(function() {
        contactsListRowClick($(this).children('td').first().text());
    });    
}

function contactsListRowClick(id) {
    log(id);
    contactEdit(id);
}
