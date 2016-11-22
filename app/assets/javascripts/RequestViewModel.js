var RequestViewModel = function(isEditable, requestModel, visitsModel, comments, personId) {
    var self = this;
    self.isEditable = isEditable;
    self.nextVisitDate = moment(requestModel.nextVisitDate).format('MMM-DD-YY HH:MM');
    self.description = ko.observable(requestModel.description);
    self.status = ko.observable(requestModel.status);
    self.rating = ko.observable(requestModel.rating);
    self.newComment = ko.observable('');
    self.newVisitCosts = ko.observable('');
    self.newVisitDate = ko.observable('');
    self.newStartDate = ko.observable('');
    self.newEndDate = ko.observable('');
    var disposables = [];
    if (isEditable) {
        self.requestModel = ko.computed(function() {
            return {
                id: requestModel.id,
                description: self.description(),
                status: self.status(),
                rating: self.rating()
            }
        });
        var debounceTimeout = 500;
        var requestModelSubscription = self.requestModel.subscribe(_.debounce(function(updatedModel) {
            $.ajax({
                url: '/api/request/' + requestModel.id,
                type: 'PUT',
                data: updatedModel
            });
        }, debounceTimeout));
        disposables = disposables.concat([self.requestModel, requestModelSubscription]);
    }
    self.visits = ko.observable({
        visits: _.map(visitsModel.visits, function (visit) {
            return {
                scheduledTime: moment(visit.scheduleTime).format('MMM-DD-YY HH:MM'),
                startTime: visit.startTime !== null ? moment(visit.startTime).format('MMM-DD-YY HH:MM') : "not defined",
                endTime: visit.endTime !== null ? moment(visit.endTime).format('MMM-DD-YY HH:MM') : "not defined",
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
        $.ajax({
            url: '/api/commented',
            type: 'POST',
            data: {
                requestId: requestModel.id,
                personId: personId,
                text: self.newComment()
            }
        }).then(function() {
            location.reload();
        });
    };
    self.dispose = function() {
        _.forEach(disposables, function(disposable){
            if (disposable && _.isFunction(disposable.dispose)){
                disposable.dispose();
            }
        });
    };
    self.addNewVisit = function () {
        $.ajax({
            url: '/api/visited/create',
            type: 'POST',
            data: {
                requestId: requestModel.id,
                costs: self.newVisitCosts,
                scheduleTime: self.newVisitDate,
                startTime: self.newStartDate,
                endTime: self.newEndDate
            }
        })
    }
};
