var RequestViewModel = function(requestModel) {
    var self = this;
    self.description = ko.observable(requestModel.description);
    self.nextVisitDate = ko.observable(moment('11/20/2016').format('MMM Do YYYY'));
    self.status = ko.observable(requestModel.status);
    self.rating = ko.observable(requestModel.rating);
    self.visits = ko.observable({
        visits: [{
            scheduledTime: moment('11/25/2016').format('MMM Do YYYY'),
            startTime: moment('11/26/2016').format('MMM Do YYYY'),
            endTime: moment('11/27/2016').format('MMM Do YYYY'),
            cost: 1500,
            assigned: 'Mr. Smith'
        }],
        totalCost: 1500});
    self.comments = ko.observableArray([{
        date: moment('11/25/2016').format('MMM Do YYYY'),
        person: 'Oleg Kulaev',
        comment: 'Vse govno peredelyvai!'
    }]);
    self.addNewComment = function(){
        alert('adding new comment');
    };
};
