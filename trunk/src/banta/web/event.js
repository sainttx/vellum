
b.event = {
    rules: {
        time: {
            minlength: 3,
            required: false,
            sanitary: true
        },
        day: {
            required: false
        },
        duration: {
            required: false,
            sanitary: true
        },
    },
    highlight: function(element) {
        console.log("highlight", element);
        $(element).closest('.control-group').removeClass('success').addClass('error');
        b.event.buttons(false);
        $(element).focus();
        b.event.errorElement = element;
    },
    success: function(element) {
        console.log("success");
        $(element).closest('.control-group').removeClass('error').addClass('success');
        b.event.buttons(true);
    },
    buttons: function(ok) {
        if (ok) {
            $('#event-save').addClass('btn-primary');
            $('#event-cancel').removeClass('btn-primary');
        } else {
            $('#event-save').removeClass('btn-primary');
            $('#event-cancel').addClass('btn-primary');
        }
    },
    loaded: function() {
        b.event.validator = $('#event-form').validate({
            rules: b.event.rules,
            highlight: b.event.highlight,
            success: b.event.success
        });
        $('#event-save').click(b.event.save);
        $('#event-add-invitee').click(b.event.addInvitee);
        $('#event-cancel').click(b.event.cancel);
        $('#event-cancel').focus(b.event.cancelFocus);
        $('#event-date-input').datepicker();
        $('#event-date-input').datepicker("option", "dateFormat", "DD, d MM, yy");
        $('#event-duration-input').autocomplete({
            source: ['30m', '45m', '1h', '2h', '3h', '4h', '1d']
        });
        $('#event-reminder-input').autocomplete({
            source: ['15m', '45m', '1h', '2h', '1d', '2d']
        });
        b.event.$tbody = $('#event-invitees-tbody');
        b.event.$tbody.hide();
        b.event.$tbody.empty();
    },
    loggedIn: function() {
        console.log('event.loggedIn', state.contacts.length);
        state.contactNames = u.array.extractValues(state.contacts, 'name');
        $('#event-host-input').autocomplete({
            source: state.contactNames
        });
        b.event.reset();
    },
    reset: function() {
        state.invitees = [];
        state.availableInvitees = state.contacts.slice(0);
    },
    removeAvailableInvitee: function(contact) {
        u.array.remove(state.availableInvitees, contact);
        $('#event-invite-input').autocomplete({
            source: state.availableInvitees
        });
    },
    addInvitee: function() {
        b.contacts.choose('eventHost', b.event.inviteeChoosen);
    },
    inviteeChoosen: function(contact) {
        console.log('event.inviteeChoosen', contact);
        state.invitees.push(contact);
        b.event.removeAvailableInvitee(contact);
    },
    cancelFocus: function() {
        if (b.event.validator.valid()) {
            $('#event-save').focus();
        }
    },
    edit: function(event) {
        state.event = event;
        console.log("event.edit", event);
        b.event.clear();
        b.event.set(event);
        $('#event-legend').text('Edit event');
        showPage('Edit event', 'event', 'event', event.name);
    },
    editNew: function() {
        setPath('event');
        console.log('event.editNew', state.contact);
        state.event = null;
        b.event.clear();
        if (state.contact) {
            $('#event-host-input').val(state.contact.name);
        }
        $('#event-legend').text('New event');
        showPage('New event', 'event', 'event', null);
        b.event.focus();
    },
    build: function(contacts) {
        contacts.sort(u.object.makeCompare('name'));
        b.event.$tbody.empty();
        for (var i = 0; i < contacts.length; i++) {
            tbody.append('<tr><td>' + contacts[i].name + '</td></tr>');
            tbody.children('tr:last').click(contacts[i], u.event.makeGetData(b.event.removeInvitee));
        }
        b.event.$tbody.show();
    },
    removeInvitee: function(contact) {
        console.log('event.r',e ceontact);
    },
    save: function(event) {
        console.log("save");
        event.preventDefault();
        b.event.errorElement = null;
        if ($('#event-form').valid()) {
            var event = b.event.get();
            console.log("save", event);
            eventsPut(event);
            server.ajax({
                url: '/event',
                data: $('#event-form').serialize(),
                success: b.event.res,
                error: b.event.error,
                memo: event
            });
        } else {
            b.event.buttons(false);
        }
    },
    res: function(res) {
        console.log('res');
        console.log(res);
        b.events.click();
    },
    error: function() {
        console.log('error');
    },
    cancel: function() {
        console.log("cancel");
        b.event.clear();
        b.events.click();
    },
    clear: function() {
        console.log("clear", $('#event-form > fieldset > .control-group').length);
        b.event.validator.resetForm();
        $('#event-cancel').addClass('btn-primary');
        $('#event-save').removeClass('btn-primary');
        b.event.buttons(false);
        $('#event-form > fieldset > div.control-group').removeClass('error');
        $('#event-form > fieldset > div.control-group').removeClass('success');
        b.event.set({
            host: '',
            time: '',
            date: '',
            day: '',
            duration: '1h'
        });
    },
    set: function(o) {
        $('#event-host-input').val(o.host);
        $('#event-time-input').val(o.time);
        $('#event-date-input').val(o.date);
        $('#event-day-input').val(o.day);
        $('#event-duration-input').val(o.duration);
    },
    get: function() {
        return {
            time: u.string.sanitize($('#event-time-input').val()),
            day: $('#event-day-input').val(),
            duration: $('#event-duration-input').val()
        };
    },
    focus: function() {
        $('#event-time-input').focus();
    },
    clickNew: function() {
        console.log('event.clickNew');
        u.event.reset();
        b.contacts.choose('eventHost', b.event.hostChosen);
    },
    hostChosen: function(contact) {
        console.log('event.chosenContactHost', contact);
        b.event.removeAvailableInvitee(contact);
        b.event.editNew();
    },
};
