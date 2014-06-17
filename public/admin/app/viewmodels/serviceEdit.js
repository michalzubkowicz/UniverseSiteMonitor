define(['knockout','backend','models/Service'], function (ko, backend,Service) {
    return {
        service:ko.observable(),
        activate:function(id){
            var l;
            if(id=="new") {
                 l = new Service(null);
            } else {
                l = new Service(id);
                l.Load();
            }

            this.service(l);
        }
    };
});