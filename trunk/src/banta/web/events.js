
function eventsLoaded() {
    $('.events-clickable').click(eventsClick);    
    dom.events = {};
    if (false) {
        $('#events-tbody span').text('');
    }
    dom.events.tbody = $('#events-tbody');
    dom.events.trHtml = dom.events.tbody.html();
}

function eventsClickable() {
    return !isEmpty(state.events);
}

function eventsClick() {
    console.log('eventsClick', state.events);
    if (isEmpty(state.events)) {
        console.warn('eventsClick no events');
    } else {
        eventsBuild();
    }
    showPage('Events', 'events', 'events', null);
}

function eventsBuild() {
    console.warn('eventsBuild', state.events.length);
    state.events.sort(compareName);
    dom.events.tbody.empty();
    for (var i = 0; i < state.events.length; i++) {
        dom.events.tbody.append(dom.events.trHtml);
        var tr = $("#events-tbody > tr:last-child");
        tr.find('span.event-contact').text(state.events[i].name);
        tr.find('span.event-time').text(u.date.format(arrayLast(state.events[i].messages).time));
        tr.find('span.event-message').text(arrayLast(state.events[i].messages).textMessage);
        tr.click(state.events[i], eventsRowClick);
    }
}

function eventsPut(event) {
    if (true) {
        return;
    }
    if (state.chat) {
        var index = eventsIndexOf(state.chat.name);
        if (index >= 0) {
            state.events[index] = event;
        }
    } else {
        var index = eventsIndexOf(event.name);
        if (index !== null && index >= 0) {
            console.log('eventsPut', event.name, index);
            state.events[index] = event;
        } else {
            state.events.push(event);
        }
    }
}

function eventsRowClick(event) {
    console.log('eventsRowClick', event.data);
    chat(event.data);
}

