var RequestViewModel = function(requestModel, visits) {
    var self = this;
    self.description = ko.observable(requestModel.description);
    self.nextVisitDate = ko.observable(moment('11/20/2016').format('MMM Do YYYY'));
    self.status = ko.observable(requestModel.status);
    self.rating = ko.observable(requestModel.rating);
    self.visits = ko.observable({
        visits: _.map(visits, function (visit) {
            return {
                scheduledTime: moment(visit.scheduleTime).format('MMM-DD-YY HH:MM'),
                startTime: moment(visit.startTime).format('MMM-DD-YY HH:MM'),
                endTime: moment(visit.endTime).format('MMM-DD-YY HH:MM'),
                cost: visit.costs
            };
        }),
        totalCost: _.reduce(visits, function(acc, visit){return acc+visit.costs;}, 0)
    });
    self.comments = ko.observableArray([{
        date: moment('11/25/2016').format('MMM Do YYYY'),
        person: 'Oleg Kulaev',
        comment: 'Vse govno peredelyvai!'
    }]);
    self.addNewComment = function(){
        alert('adding new comment');
    };
};
