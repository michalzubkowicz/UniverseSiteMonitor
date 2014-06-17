
define(['knockout','backend','models/Response', 'durandal/app'], function (ko, backend,Response,app) {
    return {
        responses: ko.observableArray([]),
        activate:function(){
            var that = this;
            return backend.getResponses().then(function(results){
                that.responses(ko.utils.arrayMap(results, function(item) {
                    var l = new Response(item._id);
                    l.fromJS(item);
                    return l;
                }));

            });
        }
    };
});