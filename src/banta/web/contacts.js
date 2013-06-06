
function contactsReady() {
    console.log("contactsReady");
    $('.contacts-clickable').click(contactsClick);
}

function contactsClick() {
    console.log("contactsClick");
    $('.page-container').hide();
    $('#contacts-container').show();
}

function showContacts(contactList) {
    buildContacts(contactList);
    $('.page-container').hide();
    $('#contacts-container').show();    
}

var contactsListHandler = {
    name: 'contacts',
    id: function(o) {
        return o.name;
    },
    columnArray: function(o) {
        return [o.name];
    },
};

function buildContacts(list) {
    contactsListHandler.list = list;
    buildTable($('#contacts-tbody'), contactListHandler);
}

function contactsListRowClick(id) {
    console.log(listHandlerFind(contactListHandler, id));
}
