
define(['knockout','backend','models/Service', 'durandal/app'], function (ko, backend,Service,app) {
    return {
        services: ko.observableArray([]),
        nextRefresh: ko.observable(10),
        interval: null,
        decrementRefresh: function() {
            var self = this;
            self.nextRefresh(self.nextRefresh()-1);
            if(self.nextRefresh()<=0) {
                self.reloadServices(true);
                self.nextRefresh(10);
            }
        },
        reloadServices: function(background) {
            var self = this;
            if(!background) showLoading();
            return backend.getServices().then(function (results) {
                self.services(ko.utils.arrayMap(results, function (item) {
                    var l = new Service(item._id);
                    l.fromJS(item);
                    return l;
                }));
                if(!background) hideLoading();
            });
        },
        activate: function () {
            var self = this;
            app.on('/admin/service/afterRemove').then(function (l) {
                self.services.remove(l);
            });

            app.on('/admin/service/afterCreate').then(function (l) {
                self.services.push(l);
            });

            self.reloadServices(false);
            self.decrementRefresh();
            self.interval=setInterval(function() {
                self.decrementRefresh();
            },1000);

            return "";
        },
        deactivate: function() {
            var self=this;
            clearInterval(self.interval);
        }

    };
});