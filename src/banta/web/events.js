

b.events = {
    loaded: function() {
        $('.events-clickable').click(b.events.click);
        $('.events-new-clickable').click(b.event.newClicked);
        if (false) {
            $('#events-tbody span').text('');
        }
        b.events.$tbody = $('#events-tbody');
        b.events.tbodyHtml = $('#events-tbody').html();
    },
    click: function() {
        console.log('click', state.events);
        if (isEmpty(state.events)) {
            console.warn('click no events');
        } else {
            b.events.build();
        }
        showPage('Events', 'events', 'events', null);
    },
    build: function() {
        console.log('events.build', state.events.length);
        state.events.sort(compareName);
        b.events.$tbody.empty();
        for (var i = 0; i < state.events.length; i++) {
            b.events.$tbody.append(b.events.tbodyHtml);
            var event = new BEvent(state.events[i]);
            console.log('events.build', event.formatDate());
            var dateRow = b.events.$tbody.find(".events-date-row").last();
            dateRow.find('span.events-day').text(event.formatDay());
            dateRow.find('span.events-date').text(event.formatDate());
            console.log("dateRow", dateRow.html());
            var infoRow = b.events.$tbody.find(".events-info-row").last();
            infoRow.find('span.events-name').text(event.name());
            infoRow.find('span.events-time').text(event.formatTime());
            infoRow.find('span.events-venue').text(event.venue());
            infoRow.find('span.event-invitees').text(event.formatInvitees());
            infoRow.click(state.events[i], b.events.chosen);
            console.log("infoRow", infoRow.html());
        }
    },
    put: function(object) {
        console.log('events.put', object);
    },
    chosen: function(e) {
        console.log('events.chosen', e.data);
        b.event.edit(e.data);
    },
};
