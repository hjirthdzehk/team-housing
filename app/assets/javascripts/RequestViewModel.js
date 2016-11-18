var RequestViewModel = function(requestModel, visitsModel, comments) {
    var self = this;
    self.description = ko.observable(requestModel.description);
    self.nextVisitDate = ko.observable(moment(requestModel.nextVisitDate).format('MMM Do YYYY'));
    self.status = ko.observable(requestModel.status);
    self.rating = ko.observable(requestModel.rating);
    self.visits = ko.observable({
        visits: _.map(visitsModel.visits, function (visit) {
            return {
                scheduledTime: moment(visit.scheduleTime).format('MMM-DD-YY HH:MM'),
                startTime: moment(visit.startTime).format('MMM-DD-YY HH:MM'),
                endTime: moment(visit.endTime).format('MMM-DD-YY HH:MM'),
                cost: visit.costs
            };
        }),
        totalCost: visitsModel.totalCost
    });
    self.comments = ko.observableArray(_.map(comments, function(comment){
        return {
            date: moment(comment.date).format('MMM-DD-YY HH:MM'),
            comment: comment.comment,
            person: comment.personName+' '+comment.personSurname
        };
    }));
    self.addNewComment = function(){
        alert('adding new comment');
    };
};
