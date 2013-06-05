
var contactListHandler = {
    name: 'org',
    id: function(org) {
        return org.orgId;
    },
    columnArray: function(org) {
        return [org.orgId, org.orgUrl, org.orgCode, org.displayName];
    },
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
        data: 'accessToken=' + server.accessToken,
        success: contactListRes,
        error: contactListError
    });
}

function contactListRes(res) {
    console.log('contactListRes');
    contactListHandler.list = res.list;
    buildTable($('#contactList-tbody'), contactListHandler);
    $('.croc-info').hide();
    $('#contactList-container').show();
}

function contactListError() {
    console.log('contactListError');
}

function contactListRowClick(id) {
    console.log(['contactListRowClick', id]);
    for (i = 0; i < contactListHandler.list.length; i++) {
        console.log(contactListHandler.list[i]);
        console.log(contactListHandler.id(contactListHandler.list[i]));
        if (contactListHandler.id(contactListHandler.list[i]) === id) {
            orgEditSet(contactListHandler.list[i]);
        }
    }
    orgEditClick();
}
