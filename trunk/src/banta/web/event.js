
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
    errorElement: null,
    validator: null,
    highlight: function(element) {
        console.log("highlight", element);
        $(element).closest('.control-group').removeClass('success').addClass('error');
        b.event.buttons(false);
        $(element).focus();
        errorElement = element;
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
        $('#event-cancel').click(b.event.cancel);
        $('#event-cancel').focus(b.event.cancelFocus);
    },
    cancelFocus: function() {
        if (b.event.validator.valid()) {
            $('#event-save').focus();
        }
    },
    edit: function(event) {
        state.event = event;
        $('.chat-clickable').addClass('btn-primary');
        $('.chat-clickable').show();
        console.log("event", event);
        b.event.clear();
        b.event.set(event);
        $('#event-legend').text('Edit event');
        showPage('Edit event', 'event', 'event', event.name);
    },
    editNew: function() {
        setPath('event');
        state.event = null;
        b.event.clear();
        $('#event-legend').text('New event');
        showPage('New event', 'event', 'event', null);
        b.event.focus();
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
            time: '',
            day: '',
            duration: ''
        });
    },
    set: function(o) {
        $('#event-name-input').val(o.name);
        $('#event-mobile-input').val(o.mobile);
        $('#event-email-input').val(o.email);
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
        b.contacts.choose('eventHost', b.event.chosenContactHost);
    },
    chosenContactHost: function(contact) {
        console.log('event.chosenContactHost', contact);
        b.event.editNew();
    },
};
