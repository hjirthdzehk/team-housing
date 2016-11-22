/**
 * Created by VladVin on 17.11.2016.
 */

var ServiceRequestsViewModel = function(requestsInfo, flatId) {
    var self = this;

    self.flatId = flatId;
    self.newRequestDescription = ko.observable('');

    self.flatNumber = requestsInfo.flatNumber >=0 ?
            requestsInfo.flatNumber :
            "unknown";

    var sreqs = requestsInfo.sreqs;
    self.serviceRequests = ko.observableArray(
        _.map(sreqs,
            function(sreq) {
                return {
                    id: sreq.id,
                    description: ko.observable(sreq.description),
                    status: ko.observable(sreq.status),
                    nextVisitDate: ko.observable(sreq.nextVisitDate !== null
                        ? moment(sreq.nextVisitDate).format('MMM-DD-YYYY HH:MM')
                        :  "not defined"),
                    totalCost: ko.observable(sreq.totalCost),
                    creationDate: ko.observable(sreq.creationDate !== null
                        ? moment(sreq.creationDate).format('MMM-DD-YYYY HH:MM')
                        : "not defined"),
                    rating: ko.observable(sreq.rating)
                }
            }
        )
    );

    self.createRequest = function () {
        if (self.newRequestDescription() !== '') {
            $.post('/api/requests/create/' + self.flatId, {
                'description':  self.newRequestDescription()
            }).then(function () {
                self.newRequestDescription('');
            });
        }
    }
};
