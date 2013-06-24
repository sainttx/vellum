

b.event = {

    eventValidatorConfig: {
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
    },

    eventErrorElement: null,

    eventValidator: null,

    eventHighlight: function(element) {
        console.log("eventHighlight", element);
        $(element).closest('.control-group').removeClass('success').addClass('error');
        b.event.eventButtons(false);
        $(element).focus();
        errorElement = element;
    },

    eventSuccess: function(element) {
        console.log("eventSuccess");
        $(element).closest('.control-group').removeClass('error').addClass('success');
        b.event.eventButtons(true);
    },

    eventButtons: function(ok) {
        if (ok) {
            $('#event-save').addClass('btn-primary');
            $('#event-cancel').removeClass('btn-primary');
        } else {
            $('#event-save').removeClass('btn-primary');
            $('#event-cancel').addClass('btn-primary');
        }
    },

    eventLoaded: function() {
        b.event.eventValidatorConfig.highlight = b.event.eventHighlight,
        b.event.eventValidatorConfig.success = b.event.eventSuccess;
        b.event.eventValidator = $('#event-form').validate(b.event.eventValidatorConfig);
        $('.event-clickable').click(b.event.eventClick);
        $('#event-save').click(b.event.eventSave);
        $('#event-cancel').click(b.event.eventCancel);
        $('#event-cancel').focus(b.event.eventCancelFocus);
    },

    eventCancelFocus: function() {
        if (b.event.eventValidator.valid()) {
            $('#event-save').focus();
        }
    },

    event: function(event) {
        state.event = event;
        $('.chat-clickable').addClass('btn-primary');
        $('.chat-clickable').show();
        console.log("event", event);
        b.event.eventClear();
        b.event.eventSet(event);
        $('#event-legend').text('Edit event');
        showPage('Edit event', 'event', 'event', event.name);
    },

    eventClick: function() {
        setPath('event');
        state.event = null;
        b.event.eventClear();
        $('#event-legend').text('New event');
        showPage('New event', 'event', 'event', null);
        b.event.eventFocus();
    },

    eventSave: function(event) {
        console.log("eventSave");
        event.preventDefault();
        b.event.eventErrorElement = null;
        if ($('#event-form').valid()) {
            var event = b.event.eventGet();
            console.log("eventSave", event);
            eventsPut(event);
            server.ajax({
                url: '/event',
                data: $('#event-form').serialize(),
                success: b.event.eventRes,
                error: b.event.eventError,
                memo: event
            });
        } else {
            b.event.eventButtons(false);
        }
    },

    eventRes: function(res) {
        console.log('eventRes');
        console.log(res);
        eventsClick();
    },

    eventError: function() {
        console.log('eventError');
    },

    eventCancel: function() {
        console.log("eventCancel");
        b.event.eventClear();
        eventsClick();
    },

    eventClear: function() {
        console.log("eventClear", $('#event-form > fieldset > .control-group').length);
        b.event.eventValidator.resetForm();
        $('#event-cancel').addClass('btn-primary');
        $('#event-save').removeClass('btn-primary');
        b.event.eventButtons(false);
        $('#event-form > fieldset > div.control-group').removeClass('error');
        $('#event-form > fieldset > div.control-group').removeClass('success');
        b.event.eventSet({
            time: '',
            day: '',
            duration: ''
        });
    },

    eventSet: function(o) {
        $('#event-name-input').val(o.name);
        $('#event-mobile-input').val(o.mobile);
        $('#event-email-input').val(o.email);
    },

    eventGet: function() {
        return {
            time: sanitize($('#event-time-input').val()),
            day: $('#event-day-input').val(),
            duration: $('#event-duration-input').val()
        };
    },

    eventFocus: function() {
        $('#event-time-input').focus();
    },

};
