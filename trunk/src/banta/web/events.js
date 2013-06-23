
function eventsLoaded() {
    $('#events-tbody span').text('');
    dom.events = {};
    dom.events.tbody = $('#events-tbody');
    dom.events.trHtml = dom.events.tbody.html();
}

function eventsClickable() {
    return !isEmpty(state.events);
}

function eventsClick() {
    console.log('eventsClick');
    if (!state.events) {
        console.warn('eventsClick');
    } else {
        eventsBuild();
    }
    showPage('Events', 'events', 'events', null);
}

function eventsBuild() {
    state.events.sort(compareName);
    dom.events.tbody.empty();
    for (var i = 0; i < state.events.length; i++) {
        dom.events.tbody.append(dom.events.trHtml);
        var tr = $("#events-tbody > tr:last-child");
        tr.find('span.event-contact').text(state.events[i].name);
        tr.find('span.event-time').text(formatDate(arrayLast(state.events[i].messages).time));
        tr.find('span.event-message').text(arrayLast(state.events[i].messages).textMessage);
        tr.click(state.events[i], eventsRowClick);
    }
}

function eventsPut(chat) {
    if (state.chat) {
        var index = eventsIndexOf(state.chat.name);
        if (index >= 0) {
            state.events[index] = chat;
        }
    } else {
        var index = eventsIndexOf(chat.name);
        if (index !== null && index >= 0) {
            console.log('eventsPut', chat.name, index);
            state.events[index] = chat;
        } else {
            state.events.push(chat);
        }
    }
}

function eventsRowClick(event) {
    console.log('eventsRowClick', event.data);
    chat(event.data);
}

