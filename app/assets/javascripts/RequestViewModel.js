var RequestViewModel = function() {
    var self = this;
    self.description = ko.observable('My toilet is broken! Im very, very, dissapointed!');
    self.nextVisitDate = ko.observable(moment('11/20/2016').format('MMM Do YYYY'));
    self.status = ko.observable(3);
    self.rating = ko.observable(2);
    self.visits = ko.observable({
        visits: [{
            scheduledTime: moment('11/25/2016').format('MMM Do YYYY'),
            startTime: moment('11/26/2016').format('MMM Do YYYY'),
            endTime: moment('11/27/2016').format('MMM Do YYYY'),
            cost: 1500,
            assigned: 'Mr. Smith'
        }],
        totalCost: 1500})
    self.comments = ko.observableArray([{
        date: moment('11/25/2016').format('MMM Do YYYY'),
        person: 'Oleg Kulaev',
        comment: 'Vse govno peredelyvai!'
    }]);
    self.addNewComment = function(){
        alert('adding new comment');
    };
};
