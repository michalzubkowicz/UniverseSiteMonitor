function Service(name,ok) {
    var self=this;
    self.name=name;
    self.ok=ok;
    self.notok=!ok;
}

function Monitor() {
    var self=this;
    self.services=ko.observableArray([]);
    self.nextRefresh = ko.observable(60);
    self.reloadServices = function() {
        $.ajax({
            url: "/services",
            type: "GET",
            context: document.body,
            success: function (r) {
                self.services($.map(r,function(s) {
                   return new Service(s.name, s.ok);
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
            self.nextRefresh(60);
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