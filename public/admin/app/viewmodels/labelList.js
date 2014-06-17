
define(['knockout','backend','models/Label', 'durandal/app'], function (ko, backend,Label,app) {
    return {
        labels: ko.observableArray([]),
        activate:function(){

            var self = this;
            app.on('/admin/label/afterRemove').then(function (l) {
                self.labels.remove(l);
            });

            app.on('/admin/label/afterCreate').then(function (l) {
                self.labels.push(l);
            });

            var that = this;
            return backend.getLabels().then(function(results){
                that.labels(ko.utils.arrayMap(results, function(item) {
                    var l = new Label(item._id);
                    l.fromJS(item);
                    return l;
                }));

            });
        }
    };
});