


function BEvent(event) {
    this.event = event;
}

BEvent.prototype = {
    formatDay: function() {
        return "Friday";
    },
    formatDate: function() {
        return "24 July";
    },
    formatTime: function() {
        return "11am";
    },
    formatInvitees: function() {
        return "Harry, Julie, George";
    },     
    host: function() {
        return "Mike";
    },     
    name: function() {
        return "Poker";
    },
    venue: function() {
        return "Mike's house";
    },
};

