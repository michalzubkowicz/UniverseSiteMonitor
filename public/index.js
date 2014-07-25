var DEFAULT_INTERVAL=10;

function Service(name,ok,seen) {
    var self=this;
    self.name=name;
    self.ok=ok;
    self.notok=!ok;
    self.seen_formatted=(seen!=null ? self.seen_formatted=new Date(seen).toLocaleString() : "")
}

function Monitor() {
    var self=this;
    self.services=ko.observableArray([]);
    self.nextRefresh = ko.observable(DEFAULT_INTERVAL);
    self.reloadServices = function() {
        $.ajax({
            url: "/services",
            type: "GET",
            context: document.body,
            success: function (r) {
                self.services($.map(r,function(s) {
                   return new Service(s.name, s.ok, s.seen);
                }));
            }
        });
    };



    self.reloadServices();

    self.decrementRefresh= function() {
        var self = this;
        self.nextRefresh(self.nextRefresh()-1);
        if(self.nextRefresh()<=0) {
            self.reloadServices();
            self.nextRefresh(DEFAULT_INTERVAL);
        }
    };

    setInterval(function() {
        self.decrementRefresh();
    },1000);
    ko.applyBindings(self);
}

$(document).ready(function () {
    var m = new Monitor();
});