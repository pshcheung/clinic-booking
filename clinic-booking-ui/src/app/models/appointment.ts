export class Appointment {
    id?: number | string;
    patientname : string = '';
    patientid : string = '';
    email : string = '';
    doctorname : string = '';
    specialization : string = '';
    date : string = '';
    startDateTime? : Date | string;
    endDateTime? : Date | string;
    age : string = '';
    gender : string = ''
    problem : string = '';
    slot : string = '';
    appointmentstatus : string = 'false';
    admissionstatus : string = 'false';

    constructor() {}
}
