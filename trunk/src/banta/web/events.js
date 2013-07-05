

b.events = {
    loaded: function() {
        $('.events-clickable').click(b.events.click);
        $('.event-new-clickable').click(b.event.newClicked);
        if (false) {
            $('#events-tbody span').text('');
        }
        b.events.tbody = $('#events-tbody');
        b.events.trHtml = b.events.tbody.html();
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
        console.warn('events.build', state.events.length);
        state.events.sort(compareName);
        b.events.tbody.empty();
        for (var i = 0; i < state.events.length; i++) {
            b.events.tbody.append(b.events.trHtml);
            var tr = $("#events-tbody > tr:last-child");
            tr.find('span.event-contact').text(state.events[i].name);
            tr.find('span.event-time').text(u.date.format(arrayLast(state.events[i].messages).time));
            tr.find('span.event-message').text(arrayLast(state.events[i].messages).textMessage);
            tr.click(state.events[i], b.events.chosen);
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
