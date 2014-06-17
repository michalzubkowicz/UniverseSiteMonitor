
define(['knockout','backend','models/Service', 'durandal/app'], function (ko, backend,Service,app) {
    return {
        services: ko.observableArray([]),
        activate: function () {

            var self = this;
            app.on('/admin/service/afterRemove').then(function (l) {
                self.services.remove(l);
            });

            app.on('/admin/service/afterCreate').then(function (l) {
                self.services.push(l);
            });

            var that = this;
            showLoading();
            return backend.getServices().then(function (results) {
                that.services(ko.utils.arrayMap(results, function (item) {
                    var l = new Service(item._id);
                    l.fromJS(item);
                    return l;
                }));
                hideLoading();
            });
        }
    };
});