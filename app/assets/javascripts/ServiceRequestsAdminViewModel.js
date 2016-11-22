/**
 * Created by VladVin on 22.11.2016.
 */

var ServiceRequestsAdminViewModel = function (requestsInfoList) {
    var self = this;

    self.newRequestDescription = ko.observable('');
    self.flatId = ko.observable('');

    self.requestsInfoList = ko.observable(requestsInfoList
        .map(function (ri) {
            return {
                flatId: ko.observable(ri.flatId),
                flatNumber: ko.observable(ri.flatNumber),
                serviceRequests: ko.observableArray(ri.sreqs
                    .map(function (sr) {
                        return {
                            id: sr.id,
                            description: ko.observable(sr.description),
                            status: ko.observable(sr.status),
                            nextVisitDate: ko.observable(moment(sr.nextVisitDate).format('MMM Do YYYY')),
                            totalCost: ko.observable(sr.totalCost),
                            creationDate: ko.observable(moment(sr.creationDate).format('MMM Do YYYY')),
                            rating: ko.observable(sr.rating)
                        }
                    })
                )
            }
        })
    );

    self.createRequest = function () {
        if (self.newRequestDescription() !== '') {
            $.post('/api/requests/create/' + self.flatId(), {
                'description':  self.newRequestDescription()
            }).then(function () {
                self.newRequestDescription('');
            });
        }
    }
};
