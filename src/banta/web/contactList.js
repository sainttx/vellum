
var contactListHandler = {
    name: 'contact',
    id: function(o) {
        return o.name;
    },
    columnArray: function(o) {
        return [o.name];
    },
    clickRow: function(o) {
      console.log('clickRow:');
      console.log(o);
    }
};

function contactListReady() {
    $('#contactList-container').load('contactList.html', function() {
        $('.contactList-clickable').click(contactListClick);
    });
}

function contactListClick() {
    server.ajax({
        type: 'POST',
        url: '/contactList',
        success: contactListRes,
        error: contactListError
    });
}

function contactListRes(res) {
    console.log('contactListRes');
    buildTable($('#contactList-tbody'), contactListHandler, res.list);
    $('.page-container').hide();
    $('#contactList-container').show();
}

function contactListError() {
    console.log('contactListError');
}

function listHandlerFind(handler, id) {
    for (i = 0; i < handler.list.length; i++) {
        if (handler.id(handler.list[i]) === id) {
            return handler.list[i];
        }
    }    
    return null;
}

function contactListRowClick(id) {
    contactEditSet(listHandlerFind(contactListHandler, id));
    contactEditClick();
}
