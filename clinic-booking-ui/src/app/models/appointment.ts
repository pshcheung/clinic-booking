export class Appointment {
    patientname : string = '';
    patientid : string = '';
    email : string = '';
    doctorname : string = '';
    specialization : string = '';
    startDateTime : Date = new Date();
    endDateTime : Date = new Date();
    age : string = '';
    gender : string = ''
    problem : string = '';
    slot : string = '';
    appointmentstatus : string = 'false';
    admissionstatus : string = 'false';

    constructor() {}
}
