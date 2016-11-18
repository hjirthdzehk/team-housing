var RequestViewModel = function(requestModel, visitsModel, comments) {
    var self = this;
    self.description = ko.observable(requestModel.description);
    self.nextVisitDate = moment(requestModel.nextVisitDate).format('MMM-DD-YY HH:MM');
    self.status = ko.observable(requestModel.status);
    self.rating = ko.observable(requestModel.rating);
    self.requestModel = ko.computed(function() {
        return {
            id: requestModel.id,
            description: self.description(),
            status: self.status(),
            rating: self.rating()
        }
    });
    var debounceTimeout = 1000;
    var requestModelSubscription = self.requestModel.subscribe(_.debounce(function(updatedModel) {
        $.ajax({
            url: '/api/request/1',
            type: 'PUT',
            data: updatedModel
        });
    }, debounceTimeout));
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
    var disposables = [self.requestModel, requestModelSubscription];
    self.dispose = function() {
        _.forEach(disposables, function(disposable){
            disposable.dispose();
        });
    }
};
