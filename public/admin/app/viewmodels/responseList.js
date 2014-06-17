
define(['knockout','backend','models/Response', 'durandal/app'], function (ko, backend,Response,app) {
    return {
        self:this,
        pageLimit: 100,
        responses: ko.observableArray([]),
        currentSkip: ko.observable(1),
        noNext: ko.observable(true),
        noPrev: ko.observable(false),
        activate:function(){
            var that = this;
            that.currentSkip.subscribe(function() {
                backend.getResponses(that.currentSkip()).then(function(results){
                    that.responses(ko.utils.arrayMap(results, function(item) {
                        var l = new Response(item._id);
                        l.fromJS(item);
                        return l;
                    }));

                    if(results.length==0) {
                        that.noNext(true);
                    } else {
                        that.noNext(false);
                    }

                    if(that.currentSkip()==0) that.noPrev(true);
                    else that.noPrev(false);
                });
            });

            //default skip
            that.currentSkip(0);
            return "";
        },
        nextPage: function() {
            this.currentSkip(this.currentSkip()+this.pageLimit);
        },
        prevPage: function() {
            var cs = this.currentSkip()-this.pageLimit;
            if(cs<0) cs=0;
            this.currentSkip(cs);
        },
        refresh: function() {
            this.currentSkip(0);
            this.currentSkip.valueHasMutated();
        }
    };
});