# BookNow
- You can find this applicaiton in [Huawei App Gallery](https://appgallery.huawei.com/app/C105867369)

> This is an appointment mobile application that allow student to book available appointment time slot by lecturer. This project used android studio to build, while using mongodb and  express js as backend. The application going to connect with backend using REST API. Backend of this project can be found in [BookNowServer](https://github.com/chan1992241/BookNowServer.git). 

## Challanges
1. Timezone Challange.
   * In the mobile application, there has features that allow lecturer user to upload available appointmenet schedule and feature allow student user to see what is the available time of the appointment. So, when user upload schedule and submit to backend to store the date time information to mongodb. However, mongodb will automatically convert the date time information to UTC format. This will cause huge issue when we retrieve the date time becuase the timezone is different because of automatic convertion of mongodb.
   > To solve this issues, I had to manually add 8 hours to date time in client since target user of this application is in this UTC+8 timezone. We didn't figure how to solve this issue if user is on another timezone.
2. Mongodb Realtime Query Challanges
   * In this application, there has features that automatically change status of appointment to "END" when appointment datetime is over current time. However, for most of my knowledge, I unable to figure out how to do it in mongodb.
   > To solve this issues, when client try to retrieve appointment data, I had to retrieve appointment data to server and manually change the status to end if the appointment time is passed.
3. Mongodb Algebra Challanges
   > Delete able schedules mean schedules that not bind with any appoinmtnet. User can choose to delete these schedules. That why it call delete able schedules.

   ![MongoDB Schema](/MISC/MongoSchema.jpg) 
   * In this application, there has features to show delete able schedules. Basically in backend, I need to retrieve schedules that not bind with any appointment.However, I am not able to figure the ways to do it in mongodb using one line query. If in SQL, I think we can achieve it using nested SQL.
   ```
   SELECT * FROM schedules s
   WHERE s._id NOT IN (
       SELECT a._id FROM appointment
   )
   ```
   > To solve this issue, I had to use two mongodb query. First, I retrieve all schedules data. Second, I retrieve appointment data. Then I filter out schedules that include in appointment data.
   ```
   const foundLecturer = await lecturerUserModel.findById(lecturerID).populate("schedules");
    if (!foundLecturer) {
        return res.status(404).send(JSON.stringify({ status: "error", message: "Lecturer not found" }));
    }
    const schedules = [];
    const foundAppointment = await AppointmentModel.find({ "lecturerID": lecturerID });
    for (let appointment of foundAppointment) {
        schedules.push(appointment.schedule.toString());
    }
    const schedulesNotInAppointment = foundLecturer.schedules.filter(schedule => {
        return !schedules.includes(schedule._id.toString());
    })
   ```
   * This issues is critical because if user deleted schedules that bind with other appointment, then the database would crash because appointment is reference with "null" or schedules that doesn't exist because being deleted.

## Future Improvement
* Implement Web Socket
* Refactor REST API