package com.example.data

data class AcademicBranch(
    val code: String,
    val name: String
)

data class AcademicPeriod(
    val periodName: String,
    val timeRange: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int
)

data class TimetableEntry(
    val period: String, // "I", "II", "III", "IV", "V", "VI", "VII"
    val time: String, // "9:10-10:00"
    val subjectCode: String, // "IAS101"
    val subjectTitle: String, // "Quantum Physics & Applications"
    val type: String, // "Lecture", "Tutorial", "Lab", "Practical"
    val teacherCode: String, // "OPS"
    val teacherName: String, // "Prof. Om Prakash Singh"
    val room: String, // "NLT-203"
    val notes: String = "", // "Merged III-IV, Sec-1"
    val day: String = "", // "MONDAY", etc.
    val section: TimetableSection = TimetableSection.COMMON
)

data class SyllabusUnit(
    val unitNumber: Int,
    val title: String,
    val weightage: String = "20%",
    val topics: List<String>
)

data class SyllabusCourse(
    val semester: Int,
    val courseCode: String,
    val title: String,
    val dept: String,
    val credits: Int,
    val ncrfHours: Int,
    val units: List<SyllabusUnit>
)

data class AcademicHoliday(
    val dateString: String, // "YYYY-MM-DD" e.g., "2026-08-15"
    val title: String,
    val description: String = ""
)

object AcademicData {

    val PERIODS = listOf(
        AcademicPeriod("I", "09:10 - 10:00", 9, 10, 10, 0),
        AcademicPeriod("II", "10:00 - 10:50", 10, 0, 10, 50),
        AcademicPeriod("III", "10:50 - 11:40", 10, 50, 11, 40),
        AcademicPeriod("IV", "11:40 - 12:30", 11, 40, 12, 30),
        AcademicPeriod("V", "14:00 - 14:50", 14, 0, 14, 50),
        AcademicPeriod("VI", "14:50 - 15:40", 14, 50, 15, 40),
        AcademicPeriod("VII", "15:40 - 16:30", 15, 40, 16, 30)
    )

    val BRANCHES = listOf(
        AcademicBranch("CS (AI)", "Computer Science (Artificial Intelligence)"),
        AcademicBranch("CS", "Computer Science"),
        AcademicBranch("CS (SF)", "Computer Science (Self-Financed)"),
        AcademicBranch("EC", "Electronics Engineering"),
        AcademicBranch("EE", "Electrical Engineering"),
        AcademicBranch("ME", "Mechanical Engineering"),
        AcademicBranch("CE", "Civil Engineering"),
        AcademicBranch("CH", "Chemical Engineering")
    )

    val TEACHER_LEGEND = mapOf(
        "OPS" to "Prof. Om Prakash Singh",
        "GF" to "Guest Faculty",
        "GF1" to "Guest Faculty 1",
        "GF2" to "Guest Faculty 2",
        "SKS" to "Prof. Sanjay K. Singh",
        "SNM" to "Dr. Suyash Narayan Mishra",
        "MK" to "Dr. Manoj Kumar",
        "PY" to "Mr. Pushpendra Yadav",
        "PS" to "Dr. Pragati Shukla",
        "AT" to "Prof. Anurag Tripathi",
        "SyS" to "Dr. Satyendra Singh",
        "SW" to "Prof. Subodh Wairiya",
        "AY" to "Er. Anurag Yadav",
        "DY" to "Mr. Deepanshu Yadav",
        "SSS" to "Mr. Shamsher Singh",
        "CF" to "Contractual Faculty",
        "CF1" to "Contractual Faculty 1 & 2",
        "KKS" to "Dr. Karunesh K. Singh",
        "AK" to "Mr. Anil Kumar",
        "AKS" to "Mr. Amitesh Kumar Singh",
        "AV" to "Mr. Anurag Verma",
        "SKT" to "Ms. Shraddha Keshav Tripathi",
        "Respective" to "Faculty Supervisor",
    )

    val COURSE_TITLE_MAP = mapOf(
        "IAS101" to "Engineering Physics",
        "IAS102" to "Engineering Chemistry",
        "IAS103" to "Engineering Mathematics-I",
        "IEE101" to "Fundamentals of Electrical Engineering",
        "IEC101" to "Fundamentals of Electronics Engineering",
        "ICS101" to "Programming for Problem Solving",
        "IME101" to "Fundamentals of Mechanical Engineering",
        "IKS101" to "Introduction to Indian Knowledge System",
        "IAI101" to "Introduction to AI & Prompt Engineering",
        "IAS104" to "Environment and Ecology",
        "IAS105" to "Soft Skills",
        "IAS151" to "Engineering Physics Lab",
        "IAS152" to "Engineering Chemistry Lab",
        "IEE151" to "Basic Electrical Engineering Lab",
        "IEC151" to "Basic Electronics Engineering Lab",
        "ICS151" to "Programming for Problem Solving Lab",
        "IAS155" to "English Language Lab",
        "ICE151" to "Engineering Graphics & Design Lab",
        "IWS151" to "Workshop Practice Lab",
        "IGP151" to "General Proficiency",
        "IAS201" to "Engineering Physics",
        "IAS202" to "Engineering Chemistry",
        "IAS203" to "Engineering Mathematics-II",
        "IEE201" to "Fundamentals of Electrical Engineering",
        "IEC201" to "Fundamentals of Electronics Engineering",
        "ICS201" to "Programming for Problem Solving",
        "IME201" to "Fundamentals of Mechanical Engineering",
        "IAS204" to "Environment and Ecology",
        "IAS205" to "Soft Skills",
        "IAS251" to "Engineering Physics Lab",
        "IAS252" to "Engineering Chemistry Lab",
        "IEE251" to "Basic Electrical Engineering Lab",
        "IEC251" to "Basic Electronics Engineering Lab",
        "ICS251" to "Programming for Problem Solving Lab",
        "IAS255" to "English Language Lab",
        "ICE251" to "Engineering Graphics & Design Lab",
        "IWS251" to "Workshop Practice Lab",
        "IVA251" to "Sports and Yoga"
    )

    fun resolveTeacherName(code: String): String {
        if (code.isBlank() || code == "XXXX") return "-"
        return TEACHER_LEGEND[code] ?: code
    }

    fun resolveCourseTitle(code: String): String {
        if (code.isBlank() || code == "XXXX") return "-"
        if (COURSE_TITLE_MAP.containsKey(code)) return COURSE_TITLE_MAP[code]!!

        val normalized = code.replace("J", "I").replace("B", "I")
        if (COURSE_TITLE_MAP.containsKey(normalized)) return COURSE_TITLE_MAP[normalized]!!

        return when {
            normalized.contains("AS101") || normalized.contains("AS201") -> "Engineering Physics"
            normalized.contains("AS102") || normalized.contains("AS202") -> "Engineering Chemistry"
            normalized.contains("AS103") -> "Engineering Mathematics-I"
            normalized.contains("AS203") -> "Engineering Mathematics-II"
            normalized.contains("EE101") || normalized.contains("EE201") -> "Fundamentals of Electrical Engineering"
            normalized.contains("EC101") || normalized.contains("EC201") -> "Fundamentals of Electronics Engineering"
            normalized.contains("CS101") || normalized.contains("CS201") -> "Programming for Problem Solving"
            normalized.contains("ME101") || normalized.contains("ME201") -> "Fundamentals of Mechanical Engineering"
            normalized.contains("KS101") -> "Introduction to Indian Knowledge System"
            normalized.contains("AS104") || normalized.contains("AS204") -> "Environment and Ecology"
            normalized.contains("AS105") || normalized.contains("AS205") -> "Soft Skills"
            normalized.contains("CE151") || normalized.contains("CE251") -> "Engineering Graphics & Design Lab"
            normalized.contains("GP151") -> "General Proficiency"
            else -> code
        }
    }

    fun getFilteredTimetable(allEntries: List<TimetableEntry>, studentSection: TimetableSection?): List<TimetableEntry> {
        return allEntries.filter { it.section == TimetableSection.COMMON || it.section == studentSection }
    }

    // ----------------------------------------------------
    // TIMETABLE DATA BY BRANCH AND DAY — VERIFIED AGAINST THE ATTACHED 9-PAGE PDF
    // ----------------------------------------------------
    val TIMETABLE: Map<String, Map<String, List<TimetableEntry>>> = mapOf(
        "CS (AI)" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Lecture", "OPS", "Prof. Om Prakash Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("II", "10:00 - 10:50", "JKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("III", "10:50 - 11:40", "JAS151", "Sec 1 Phy Lab", "Lab", "OPS/GF", "Prof. Om Prakash Singh / Guest Faculty", "Phy Lab", section = TimetableSection.SECTION_1),
                TimetableEntry("III", "10:50 - 11:40", "JEE151", "Sec 2 EE Lab", "Lab", "SyS", "Dr. Satyendra Singh", "BEE Lab", section = TimetableSection.SECTION_2),
                TimetableEntry("IV", "11:40 - 12:30", "JAS151", "Sec 1 Phy Lab", "Lab", "OPS/GF", "Prof. Om Prakash Singh / Guest Faculty", "Phy Lab", section = TimetableSection.SECTION_1),
                TimetableEntry("IV", "11:40 - 12:30", "JEE151", "Sec 2 EE Lab", "Lab", "SyS", "Dr. Satyendra Singh", "BEE Lab", section = TimetableSection.SECTION_2),
                TimetableEntry("V", "14:00 - 14:50", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VII", "15:40 - 16:30", "JCS101", "Programming for Problem Solving", "Tutorial T2", "DY", "Mr. Deepanshu Yadav", "NLT-203", section = TimetableSection.COMMON)
            ),
            "TUESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "JCS101", "Programming for Problem Solving", "Lecture", "DY", "Mr. Deepanshu Yadav", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("II", "10:00 - 10:50", "JCS101", "Programming for Problem Solving", "Lecture", "DY", "Mr. Deepanshu Yadav", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("III", "10:50 - 11:40", "IAS101", "Engineering Physics", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("V", "14:00 - 14:50", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "AKS", "Mr. Amitesh Kumar Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VI", "14:50 - 15:40", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "SyS", "Dr. Satyendra Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VII", "15:40 - 16:30", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "SyS", "Dr. Satyendra Singh", "NLT-203", section = TimetableSection.COMMON)
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "JKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("III", "10:50 - 11:40", "JAS151", "Sec 2 Phy Lab", "Lab", "OPS/GF", "Prof. Om Prakash Singh / Guest Faculty", "Phy Lab", section = TimetableSection.SECTION_2),
                TimetableEntry("III", "10:50 - 11:40", "JEE151", "Sec 1 EE Lab", "Lab", "SyS", "Dr. Satyendra Singh", "BEE Lab", section = TimetableSection.SECTION_1),
                TimetableEntry("IV", "11:40 - 12:30", "JAS151", "Sec 2 Phy Lab", "Lab", "OPS/GF", "Prof. Om Prakash Singh / Guest Faculty", "Phy Lab", section = TimetableSection.SECTION_2),
                TimetableEntry("IV", "11:40 - 12:30", "JEE151", "Sec 1 EE Lab", "Lab", "SyS", "Dr. Satyendra Singh", "BEE Lab", section = TimetableSection.SECTION_1)
            ),
            "THURSDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "JCE151", "Sec 2 Engg Graphics & Design Lab", "Lab", "Respective", "Faculty Supervisor", "Graphics and design lab", section = TimetableSection.SECTION_2),
                TimetableEntry("I", "09:10 - 10:00", "JCS151", "Sec 1 Programming", "Lab", "DY", "Mr. Deepanshu Yadav", "Computer Lab", section = TimetableSection.SECTION_1),
                TimetableEntry("II", "10:00 - 10:50", "JCE151", "Sec 2 Engg Graphics & Design Lab", "Lab", "Respective", "Faculty Supervisor", "Graphics and design lab", section = TimetableSection.SECTION_2),
                TimetableEntry("II", "10:00 - 10:50", "JCS151", "Sec 1 Programming", "Lab", "DY", "Mr. Deepanshu Yadav", "Computer Lab", section = TimetableSection.SECTION_1),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "SNM/AKS", "Dr. Suyash Narayan Mishra / Mr. Amitesh Kumar Singh", "NLT-101", section = TimetableSection.COMMON),
                TimetableEntry("IV", "11:40 - 12:30", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-101", section = TimetableSection.COMMON),
                TimetableEntry("V", "14:00 - 14:50", "IAS104", "Environment and Ecology", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VI", "14:50 - 15:40", "IAS104", "Environment and Ecology", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-203", section = TimetableSection.COMMON)
            ),
            "FRIDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "JCS101", "Programming for Problem Solving", "Tutorial T1", "DY", "Mr. Deepanshu Yadav", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("III", "10:50 - 11:40", "IAS101", "Engineering Physics", "Tutorial T2", "GF", "Guest Faculty", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("IV", "11:40 - 12:30", "IAS101", "Engineering Physics", "Tutorial T1", "CF", "Contractual Faculty", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VI", "14:50 - 15:40", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T2", "SyS", "Dr. Satyendra Singh", "NLT-203", section = TimetableSection.COMMON),
                TimetableEntry("VII", "15:40 - 16:30", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T1", "SyS", "Dr. Satyendra Singh", "NLT-203", section = TimetableSection.COMMON)
            ),
            "SATURDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "JCE151", "Sec 1 Engg Graphics & Design Lab", "Lab", "Respective", "Faculty Supervisor", "Graphics lab", section = TimetableSection.SECTION_1),
                TimetableEntry("II", "10:00 - 10:50", "JCS151", "Sec 2 Programming", "Lab", "DY", "Mr. Deepanshu Yadav", "Computer Lab", section = TimetableSection.SECTION_2),
                TimetableEntry("III", "10:50 - 11:40", "JCE151", "Sec 1 Engg Graphics & Design Lab", "Lab", "Respective", "Faculty Supervisor", "Graphics lab", section = TimetableSection.SECTION_1),
                TimetableEntry("III", "10:50 - 11:40", "JCS151", "Sec 2 Programming", "Lab", "DY", "Mr. Deepanshu Yadav", "Computer Lab", section = TimetableSection.SECTION_2)
            )
        ),
        "ME" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Tutorial T2", "GF", "Guest Faculty", "NLT-102"),
                TimetableEntry("II", "10:00 - 10:50", "ICS101", "Programming for Problem Solving", "Lecture", "SKT", "Ms. Shraddha Keshav Tripathi", "NLT-102", "Merged II-III"),
                TimetableEntry("III", "10:50 - 11:40", "ICS101", "Programming for Problem Solving", "Lecture", "SKT", "Ms. Shraddha Keshav Tripathi", "NLT-102", "Merged II-III"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS104", "Environment and Ecology", "Lecture", "GF1", "Guest Faculty 1", "NLT-102"),
                TimetableEntry("V", "14:00 - 14:50", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-102", "Merged V-VI"),
                TimetableEntry("VI", "14:50 - 15:40", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-102", "Merged V-VI"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "MK/AKS", "Dr. Manoj Kumar / Mr. Amitesh Kumar Singh", "NLT-102")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/GF / AV", "Prof. Sanjay K. Singh / Guest Faculty / Mr. Anurag Verma", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/GF / AV", "Prof. Sanjay K. Singh / Guest Faculty / Mr. Anurag Verma", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-102"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS101", "Engineering Physics", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-102"),
                TimetableEntry("VI", "14:50 - 15:40", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SKT", "Ms. Shraddha Keshav Tripathi", "Computer Lab", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SKT", "Ms. Shraddha Keshav Tripathi", "Computer Lab", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Lecture", "OPS", "Prof. Om Prakash Singh", "NLT-102"),
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-102"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-102")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "ICS101", "Programming for Problem Solving", "Tutorial T1", "SKT", "Ms. Shraddha Keshav Tripathi", "NLT-102"),
                TimetableEntry("III", "10:50 - 11:40", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AV", "Mr. Anurag Verma", "NLT-102"),
                TimetableEntry("IV", "11:40 - 12:30", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T1", "AV", "Mr. Anurag Verma", "NLT-102"),
                TimetableEntry("VI", "14:50 - 15:40", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/GF / AV", "Prof. Sanjay K. Singh / Guest Faculty / Mr. Anurag Verma", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/GF / AV", "Prof. Sanjay K. Singh / Guest Faculty / Mr. Anurag Verma", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SKT", "Ms. Shraddha Keshav Tripathi", "Computer Lab", "Sec-1 ICE151 / Sec-2 ICS151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SKT", "Ms. Shraddha Keshav Tripathi", "Computer Lab", "Sec-1 ICE151 / Sec-2 ICS151; merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAS104", "Environment and Ecology", "Lecture", "SS", "Prof. Sanjay Srivastava", "NLT-102"),
                TimetableEntry("VI", "14:50 - 15:40", "ICS101", "Programming for Problem Solving", "Tutorial T2", "SKT", "Ms. Shraddha Keshav Tripathi", "NLT-102")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Lecture", "CF", "Contractual Faculty", "NLT-102"),
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "MK/PY", "Dr. Manoj Kumar / Mr. Pushpendra Yadav", "NLT-102"),
                TimetableEntry("III", "10:50 - 11:40", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T2", "AV", "Mr. Anurag Verma", "NLT-102"),
                TimetableEntry("IV", "11:40 - 12:30", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AV", "Mr. Anurag Verma", "NLT-102")
            )
        ),
        "EE" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "AKS", "Mr. Amitesh Kumar Singh", "Computer Lab", "Sec-1 ICE151 / Sec-2 ICS151; merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "AKS", "Mr. Amitesh Kumar Singh", "Computer Lab", "Sec-1 ICE151 / Sec-2 ICS151; merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AT", "Prof. Anurag Tripathi", "NLT-203", "Merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AT", "Prof. Anurag Tripathi", "NLT-203", "Merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-201")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-102"),
                TimetableEntry("II", "10:00 - 10:50", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-102"),
                TimetableEntry("III", "10:50 - 11:40", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "OPS/CF / AT", "Prof. Om Prakash Singh / Contractual Faculty / Prof. Anurag Tripathi", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "OPS/CF / AT", "Prof. Om Prakash Singh / Contractual Faculty / Prof. Anurag Tripathi", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged III-IV"),
                TimetableEntry("VI", "14:50 - 15:40", "ICS101", "Programming for Problem Solving", "Tutorial T1", "AKS", "Mr. Amitesh Kumar Singh", "NLT-201"),
                TimetableEntry("VII", "15:40 - 16:30", "ICS101", "Programming for Problem Solving", "Tutorial T2", "AKS", "Mr. Amitesh Kumar Singh", "NLT-201")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-201"),
                TimetableEntry("IV", "11:40 - 12:30", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T1", "AT", "Prof. Anurag Tripathi", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "ICS101", "Programming for Problem Solving", "Lecture", "AKS", "Mr. Amitesh Kumar Singh", "NLT-201"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS104", "Environment and Ecology", "Lecture", "GF1", "Guest Faculty 1", "NLT-201")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Lecture", "OPS", "Prof. Om Prakash Singh", "NLT-101"),
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-101"),
                TimetableEntry("III", "10:50 - 11:40", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "OPS/CF / AT", "Prof. Om Prakash Singh / Contractual Faculty / Prof. Anurag Tripathi", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "OPS/CF / AT", "Prof. Om Prakash Singh / Contractual Faculty / Prof. Anurag Tripathi", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged III-IV"),
                TimetableEntry("VI", "14:50 - 15:40", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "AKS", "Mr. Amitesh Kumar Singh", "Computer Lab", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "AKS", "Mr. Amitesh Kumar Singh", "Computer Lab", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS101", "Engineering Physics", "Tutorial T1", "CF", "Contractual Faculty", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IAS101", "Engineering Physics", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-201"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS101", "Engineering Physics", "Tutorial T2", "GF", "Guest Faculty", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "ICS101", "Programming for Problem Solving", "Lecture", "AKS", "Mr. Amitesh Kumar Singh", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T2", "AT", "Prof. Anurag Tripathi", "NLT-201"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS104", "Environment and Ecology", "Lecture", "SS", "Prof. Sanjay Srivastava", "NLT-201")
            )
        ),
        "EC" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS101", "Engineering Physics", "Tutorial T1", "GF", "Guest Faculty", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-201", "Merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IEE101", "Fundamentals of Electrical Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-201", "Merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAS104", "Environment and Ecology", "Lecture", "GF2", "Guest Faculty 2", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "ICS101", "Programming for Problem Solving", "Lecture", "SSS", "Mr. Shamsher Singh", "NLT-101", "Merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "ICS101", "Programming for Problem Solving", "Lecture", "SSS", "Mr. Shamsher Singh", "NLT-101", "Merged VI-VII")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS101", "Engineering Physics", "Lecture", "OPS", "Prof. Om Prakash Singh", "NLT-201"),
                TimetableEntry("II", "10:00 - 10:50", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-201"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/CF / AK", "Prof. Sanjay K. Singh / Contractual Faculty / Mr. Anil Kumar", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/CF / AK", "Prof. Sanjay K. Singh / Contractual Faculty / Mr. Anil Kumar", "BEE Lab", "Sec-1 IAS151 / Sec-2 IEE151; merged VI-VII")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SSS", "Mr. Shamsher Singh", "-", "Sec-1 ICE151 / Sec-2 ICS151; merged II-III"),
                TimetableEntry("III", "10:50 - 11:40", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SSS", "Mr. Shamsher Singh", "-", "Sec-1 ICE151 / Sec-2 ICS151; merged II-III"),
                TimetableEntry("V", "14:00 - 14:50", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T2", "AK", "Mr. Anil Kumar", "NLT-102"),
                TimetableEntry("VI", "14:50 - 15:40", "IEE101", "Fundamentals of Electrical Engineering", "Tutorial T1", "AK", "Mr. Anil Kumar", "NLT-102"),
                TimetableEntry("VII", "15:40 - 16:30", "IKS101", "Introduction to Indian Knowledge System", "Lecture", "SKS", "Dr. Santosh K. Singh", "NLT-102")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS104", "Environment and Ecology", "Lecture", "GF2", "Guest Faculty 2", "NLT-201"),
                TimetableEntry("II", "10:00 - 10:50", "IAS101", "Engineering Physics", "Tutorial T2", "CF", "Contractual Faculty", "NLT-201"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "AKS", "Mr. Amitesh Kumar Singh", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/CF / AK", "Prof. Sanjay K. Singh / Contractual Faculty / Mr. Anil Kumar", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS151/IEE151", "Engineering Physics Lab / Basic Electrical Engineering Lab", "Lab", "SKS/CF / AK", "Prof. Sanjay K. Singh / Contractual Faculty / Mr. Anil Kumar", "BEE Lab", "Sec-2 IAS151 / Sec-1 IEE151; merged VI-VII")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "SNM/AKS", "Dr. Suyash Narayan Mishra / Mr. Amitesh Kumar Singh", "NLT-102"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "SNM", "Dr. Suyash Narayan Mishra", "NLT-102"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS101", "Engineering Physics", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-102"),
                TimetableEntry("VI", "14:50 - 15:40", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SSS", "Mr. Shamsher Singh", "-", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "ICE151/ICS151", "Engineering Graphics & Design Lab / Programming for Problem Solving Lab", "Lab", "SSS", "Mr. Shamsher Singh", "-", "Sec-2 ICE151 / Sec-1 ICS151; merged VI-VII")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "ICS101", "Programming for Problem Solving", "Tutorial T1", "SSS", "Mr. Shamsher Singh", "NLT-102"),
                TimetableEntry("IV", "11:40 - 12:30", "ICS101", "Programming for Problem Solving", "Tutorial T2", "SSS", "Mr. Shamsher Singh", "NLT-102")
            )
        ),
        "CS (SF)" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "SS/GF1 / SW", "Prof. Sanjay Srivastava / Guest Faculty 1 / Prof. Subodh Wairiya", "BEC Lab", "Sec-1 IAS152 / Sec-2 IEC151; merged II-III"),
                TimetableEntry("III", "10:50 - 11:40", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "SS/GF1 / SW", "Prof. Sanjay Srivastava / Guest Faculty 1 / Prof. Subodh Wairiya", "BEC Lab", "Sec-1 IAS152 / Sec-2 IEC151; merged II-III"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS102", "Engineering Chemistry", "Tutorial T2", "GF2", "Guest Faculty 2", "NLT-202"),
                TimetableEntry("V", "14:00 - 14:50", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "MK/PY", "Dr. Manoj Kumar / Mr. Pushpendra Yadav", "NLT-202"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "MK/PY", "Dr. Manoj Kumar / Mr. Pushpendra Yadav", "NLT-202")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("III", "10:50 - 11:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "SW", "Prof. Subodh Wairiya", "NLT-202"),
                TimetableEntry("VI", "14:50 - 15:40", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("VII", "15:40 - 16:30", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-202")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T2", "Respective", "Faculty", "NLT-203"),
                TimetableEntry("III", "10:50 - 11:40", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T2", "SW", "Prof. Subodh Wairiya", "NLT-203"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T1", "SW", "Prof. Subodh Wairiya", "NLT-203"),
                TimetableEntry("V", "14:00 - 14:50", "ICE151/IAS152", "Engineering Graphics & Design Lab / Engineering Chemistry Lab", "Lab", "SW / SS/GF1", "Prof. Subodh Wairiya / Prof. Sanjay Srivastava / Guest Faculty 1", "-", "Sec-1 ICE151 / Sec-2 IAS152")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS105", "Soft Skills", "Lecture", "PS", "Dr. Pragati Shukla", "NLT-202"),
                TimetableEntry("III", "10:50 - 11:40", "IAS105", "Soft Skills", "Lecture", "PS", "Dr. Pragati Shukla", "NLT-202"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS102", "Engineering Chemistry", "Lecture", "SS", "Prof. Sanjay Srivastava", "NLT-202"),
                TimetableEntry("V", "14:00 - 14:50", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged V-VI"),
                TimetableEntry("VI", "14:50 - 15:40", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged V-VI")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS102", "Engineering Chemistry", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-202"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-202"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-202"),
                TimetableEntry("V", "14:00 - 14:50", "IAS102", "Engineering Chemistry", "Tutorial T1", "GF1", "Guest Faculty 1", "NLT-201"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged VI-VII")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "PY", "Mr. Pushpendra Yadav", "NLT-202"),
                TimetableEntry("IV", "11:40 - 12:30", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T1", "Respective", "Faculty", "NLT-202")
            )
        ),
        "CH" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AY", "Er. Anurag Yadav", "NLT-101"),
                TimetableEntry("III", "10:50 - 11:40", "IAS102", "Engineering Chemistry", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-101"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS102", "Engineering Chemistry", "Lecture", "SS", "Prof. Sanjay Srivastava", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("VII", "15:40 - 16:30", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-204")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-101"),
                TimetableEntry("III", "10:50 - 11:40", "IAS105", "Soft Skills", "Lecture", "PS", "Dr. Pragati Shukla", "NLT-101"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS105", "Soft Skills", "Lecture", "PS", "Dr. Pragati Shukla", "NLT-101"),
                TimetableEntry("V", "14:00 - 14:50", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T1", "Respective", "Faculty", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "MK/PY", "Dr. Manoj Kumar / Mr. Pushpendra Yadav", "NLT-101"),
                TimetableEntry("VII", "15:40 - 16:30", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-101")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "GF1/GF2 / AY", "Guest Faculty 1 / Guest Faculty 2 / Er. Anurag Yadav", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "GF1/GF2 / AY", "Guest Faculty 1 / Guest Faculty 2 / Er. Anurag Yadav", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF1", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF1", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AY", "Er. Anurag Yadav", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T1", "AY", "Er. Anurag Yadav", "NLT-101")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-203"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "MK", "Dr. Manoj Kumar", "NLT-203"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T2", "AY", "Er. Anurag Yadav", "NLT-203"),
                TimetableEntry("V", "14:00 - 14:50", "IAS102", "Engineering Chemistry", "Tutorial T2", "GF2", "Guest Faculty 2", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T2", "Respective", "Faculty", "NLT-101")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("III", "10:50 - 11:40", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "AY/SS/GF1", "Er. Anurag Yadav / Prof. Sanjay Srivastava / Guest Faculty 1", "-", "Sec-2 IAS152 / Sec-1 IEC151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "AY/SS/GF1", "Er. Anurag Yadav / Prof. Sanjay Srivastava / Guest Faculty 1", "-", "Sec-2 IAS152 / Sec-1 IEC151; merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-101"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS102", "Engineering Chemistry", "Tutorial T1", "GF1", "Guest Faculty 1", "NLT-101"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "MK/PY", "Dr. Manoj Kumar / Mr. Pushpendra Yadav", "NLT-101")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF1", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF1", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged III-IV")
            )
        ),
        "CS" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS105", "Soft Skills", "Lecture", "CF1", "Contractual Faculty 1 & 2", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-204", "Merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-204", "Merged III-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAS102", "Engineering Chemistry", "Tutorial T1", "GF1", "Guest Faculty 1", "NLT-204"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged VI-VII")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "SKS/GF2 / AK", "Prof. Sanjay K. Singh / Guest Faculty 2 / Mr. Anil Kumar", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged II-III"),
                TimetableEntry("III", "10:50 - 11:40", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "SKS/GF2 / AK", "Prof. Sanjay K. Singh / Guest Faculty 2 / Mr. Anil Kumar", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged II-III"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS102", "Engineering Chemistry", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-202"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-204"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-204")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS105", "Soft Skills", "Lecture", "CF1", "Contractual Faculty 1 & 2", "NLT-204"),
                TimetableEntry("II", "10:00 - 10:50", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("IV", "11:40 - 12:30", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("V", "14:00 - 14:50", "IAS102", "Engineering Chemistry", "Lecture", "SS", "Prof. Sanjay Srivastava", "NLT-204"),
                TimetableEntry("VI", "14:50 - 15:40", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged VI-VII")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "CF1", "Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IEC151/IAS152", "Basic Electronics Engineering Lab / Engineering Chemistry Lab", "Lab", "AK / SKS/GF2", "Mr. Anil Kumar / Prof. Sanjay K. Singh / Guest Faculty 2", "-", "Sec-1 IEC151 / Sec-2 IAS152; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC151/IAS152", "Basic Electronics Engineering Lab / Engineering Chemistry Lab", "Lab", "AK / SKS/GF2", "Mr. Anil Kumar / Prof. Sanjay K. Singh / Guest Faculty 2", "-", "Sec-1 IEC151 / Sec-2 IAS152; merged III-IV"),
                TimetableEntry("VI", "14:50 - 15:40", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T1", "AK", "Mr. Anil Kumar", "NLT-204"),
                TimetableEntry("VII", "15:40 - 16:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T2", "AK", "Mr. Anil Kumar", "NLT-204")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T1", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "AKS", "Mr. Amitesh Kumar Singh", "NLT-204"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS102", "Engineering Chemistry", "Tutorial T2", "GF2", "Guest Faculty 2", "NLT-204"),
                TimetableEntry("V", "14:00 - 14:50", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T2", "Respective", "Faculty", "NLT-204"),
                TimetableEntry("VI", "14:50 - 15:40", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-204")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-204"),
                TimetableEntry("IV", "11:40 - 12:30", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-204")
            )
        ),
        "CE" to mapOf(
            "MONDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("II", "10:00 - 10:50", "IGP151", "General Proficiency", "Activity", "Respective", "Faculty Supervisor", "-", "Merged I-II"),
                TimetableEntry("III", "10:50 - 11:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF2", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged III-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF2", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-1 IAS155 / Sec-2 IWS151; merged III-IV"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "GF1/GF2 / AK", "Guest Faculty 1 / Guest Faculty 2 / Mr. Anil Kumar", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS152/IEC151", "Engineering Chemistry Lab / Basic Electronics Engineering Lab", "Lab", "GF1/GF2 / AK", "Guest Faculty 1 / Guest Faculty 2 / Mr. Anil Kumar", "-", "Sec-1 IAS152 / Sec-2 IEC151; merged VI-VII")
            ),
            "TUESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-203"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T1", "AK", "Mr. Anil Kumar", "NLT-203"),
                TimetableEntry("VI", "14:50 - 15:40", "IEC151/IAS152", "Basic Electronics Engineering Lab / Engineering Chemistry Lab", "Lab", "AK / SKS/GF2", "Mr. Anil Kumar / Prof. Sanjay K. Singh / Guest Faculty 2", "-", "Sec-1 IEC151 / Sec-2 IAS152; merged VI-VII"),
                TimetableEntry("VII", "15:40 - 16:30", "IEC151/IAS152", "Basic Electronics Engineering Lab / Engineering Chemistry Lab", "Lab", "AK / SKS/GF2", "Mr. Anil Kumar / Prof. Sanjay K. Singh / Guest Faculty 2", "-", "Sec-1 IEC151 / Sec-2 IAS152; merged VI-VII")
            ),
            "WEDNESDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T1", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("III", "10:50 - 11:40", "IME101", "Fundamentals of Mechanical Engineering", "Tutorial T2", "Respective", "Faculty", "NLT-202"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T2", "AK", "Mr. Anil Kumar", "NLT-202"),
                TimetableEntry("V", "14:00 - 14:50", "IAS105", "Soft Skills", "Lecture", "CF1", "Contractual Faculty 1 & 2", "NLT-202"),
                TimetableEntry("VI", "14:50 - 15:40", "IAS103", "Engineering Mathematics-I", "Tutorial T2", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-202"),
                TimetableEntry("VII", "15:40 - 16:30", "IAS102", "Engineering Chemistry", "Tutorial T1", "GF1", "Guest Faculty 1", "NLT-202")
            ),
            "THURSDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS102", "Engineering Chemistry", "Lecture", "SKS", "Prof. Sanjay K. Singh", "NLT-204"),
                TimetableEntry("III", "10:50 - 11:40", "IAS103", "Engineering Mathematics-I", "Lecture", "KKS", "Dr. Karunesh K. Singh", "NLT-204"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS103", "Engineering Mathematics-I", "Tutorial T1", "KKS/AK", "Dr. Karunesh K. Singh / Mr. Anil Kumar", "NLT-204"),
                TimetableEntry("V", "14:00 - 14:50", "IME101", "Fundamentals of Mechanical Engineering", "Lecture", "Respective", "Faculty", "NLT-102")
            ),
            "FRIDAY" to listOf(
                TimetableEntry("I", "09:10 - 10:00", "IAI101", "Introduction to AI & Prompt Engineering", "Lecture", "Respective", "Faculty", "NLT-101"),
                TimetableEntry("II", "10:00 - 10:50", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF2", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged II-IV"),
                TimetableEntry("III", "10:50 - 11:40", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF2", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged II-IV"),
                TimetableEntry("IV", "11:40 - 12:30", "IAS155/IWS151", "English Language Lab / Workshop Practice Lab", "Lab", "PS/CF2", "Dr. Pragati Shukla / Contractual Faculty 1 & 2", "-", "Sec-2 IAS155 / Sec-1 IWS151; merged II-IV"),
                TimetableEntry("V", "14:00 - 14:50", "IAS102", "Engineering Chemistry", "Tutorial T2", "GF2", "Guest Faculty 2", "NLT-202"),
                TimetableEntry("VI", "14:50 - 15:40", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-202")
            ),
            "SATURDAY" to listOf(
                TimetableEntry("II", "10:00 - 10:50", "IAS105", "Soft Skills", "Lecture", "CF1", "Contractual Faculty 1 & 2", "NLT-203"),
                TimetableEntry("III", "10:50 - 11:40", "IEC101", "Fundamentals of Electronics Engineering", "Lecture", "AK", "Mr. Anil Kumar", "NLT-203"),
                TimetableEntry("IV", "11:40 - 12:30", "IEC101", "Fundamentals of Electronics Engineering", "Tutorial T1", "AK", "Mr. Anil Kumar", "NLT-203")
            )
        )
    )

    // ----------------------------------------------------
    // SYLLABUS DATA (From Syllabus Reference Guide PDF #1)
    // ----------------------------------------------------
    val SYLLABUS: List<SyllabusCourse> = listOf(
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS101",
            title = "Engineering Physics",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Quantum Mechanics",
                    weightage = "20%",
                    topics = listOf(
                        "Inadequacy of classical mechanics.",
                        "Planck’s theory of black body radiation (qualitative).",
                        "Compton effect.",
                        "de-Broglie concept of matter waves.",
                        "Davisson and Germer Experiment.",
                        "Phase velocity and group velocity.",
                        "Time-dependent and time-independent Schrödinger wave equations.",
                        "Physical interpretation of wave function.",
                        "Particle in a one-Dimensional box."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Electromagnetic Field Theory",
                    weightage = "20%",
                    topics = listOf(
                        "Basic concept of Stoke’s theorem and Divergence theorem.",
                        "Basic laws of electricity and magnetism.",
                        "Continuity equation for current density.",
                        "Displacement current.",
                        "Maxwell equations in integral and differential form.",
                        "Maxwell equations in vacuum and in conducting medium.",
                        "Poynting vector and Poynting theorem.",
                        "Plane electromagnetic waves in vacuum and their transverse nature.",
                        "Relation between electric and magnetic fields of an electromagnetic wave.",
                        "Plane electromagnetic waves in conducting medium.",
                        "Skin depth."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Wave Optics",
                    weightage = "20%",
                    topics = listOf(
                        "Coherent sources.",
                        "Interference in uniform and wedge shaped thin films.",
                        "Colours of thin films.",
                        "Necessity of extended sources.",
                        "Newton’s Rings and its applications (Wavelength and refractive index).",
                        "Introduction to diffraction.",
                        "Fraunhoffer diffraction at single slit and double slit.",
                        "Absent spectra.",
                        "Diffraction grating.",
                        "Spectra with grating.",
                        "Maximum numbers of orders with grating.",
                        "Absent spectra with grating.",
                        "Dispersive power of grating."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Fibre Optics & Laser",
                    weightage = "20%",
                    topics = listOf(
                        "Fibre Optics: Principle and construction of optical fibre, Acceptance angle, Numerical aperture, Acceptance cone, Step index and graded index fibres, Fibre optic communication principle, Attenuation, Application of fibre.",
                        "Laser: Absorption of radiation, Spontaneous and stimulated emission of radiation, Population inversion, Einstein’s Coefficients, Principles of laser action, Solid state Laser (Ruby laser) and Gas Laser (He-Ne laser), Laser applications."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Superconductors and Nano-Materials",
                    weightage = "20%",
                    topics = listOf(
                        "Superconductors: Temperature dependence of resistivity in superconducting materials, Meissner effect, Temperature dependence of critical field, Persistent current, Type I and Type II superconductors, Properties and Applications of Super-conductors.",
                        "Nano-Materials: Introduction and properties of nano materials, Basic concept of Quantum Dots, Quantum wires and Quantum well, Fabrication of nano materials - Top-Down approach (CVD) and Bottom-Up approach (Sol Gel), Properties and Applications of nano materials."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS102",
            title = "Engineering Chemistry",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Solid State Chemistry, Advanced Materials, and Green Chemistry",
                    weightage = "20%",
                    topics = listOf(
                        "Solid State Chemistry: Introduction, Crystal Imperfections; Stoichiometric Defects and Nonstoichiometric Defects.",
                        "Chemistry of Advanced Materials: Liquid Crystals; Introduction, Types and Applications of liquid crystals, Industrially important materials used as liquid crystals.",
                        "Graphite and Fullerene: Introduction, Structure and applications.",
                        "Nanomaterials: Introduction, Preparation, characteristics of nanomaterials and applications of nanomaterials, Carbon Nano Tubes (CNT).",
                        "Green Chemistry: Introduction, 12 principles and importance of green synthesis, Green Chemicals, Synthesis of typical organic compounds by conventional and Green route (Adipic acid and Paracetamol), Environmental impact of Green chemistry on society."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Spectroscopic Techniques and Applications",
                    weightage = "20%",
                    topics = listOf(
                        "Elementary idea and simple applications of UV, IR and NMR.",
                        "Numerical problems.",
                        "Stereochemistry: Optical isomerism in compounds without chiral carbon, Geometrical isomerism, Chiral Drugs."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Named Reaction, Catalysis, and Corrosion",
                    weightage = "20%",
                    topics = listOf(
                        "Named Reaction their Mechanism and Industrial Applications: Beckmann Reaction, Diel’s Alder Reaction, Cannizzaro Reaction, Aldol Condensation, Reimer Tiemann reaction, Hofmann Reaction and Friedel Craft Reaction.",
                        "Catalysis: Introduction; Characteristics of catalyst, Types of catalyst, Theories of catalysis, Positive and Negative catalyst, Catalytic promoters and Inhibitors, Autocatalyst, Enzyme catalyst and Industrial catalyst.",
                        "Corrosion: Introduction to corrosion, Types of corrosion, Cause of corrosion, Corrosion prevention and control, Corrosion issues in specific industries (Power generation, Chemical processing industry, Oil & gas industry and Pulp & paper industries).",
                        "Chemistry of Engineering Materials: Cement; Constituents, manufacturing, hardening and setting, deterioration of cement, Plaster of Paris (POP)."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Water Technology and Fuels",
                    weightage = "20%",
                    topics = listOf(
                        "Water Technology: Sources and impurities of water, Hardness of water, Boiler troubles, Techniques for water softening (Lime-Soda, Zeolite, Ion Exchange and Reverse Osmosis process), Determination of Hardness and alkalinity, Numerical problems.",
                        "Fuels and Combustion: Definition, Classification, Characteristics of a good fuel, Calorific Values, Gross & Net calorific value, Determination of calorific value by Bomb Calorimeter, Theoretical calculation of calorific value by Dulong’s method, Ranking of Coal, Analysis of coal by Proximate and Ultimate analysis method, Numerical problems, Chemistry of Biogas production from organic waste materials and their environmental impact on society."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Materials Chemistry",
                    weightage = "20%",
                    topics = listOf(
                        "Polymers: Classification, Polymerisation processes, Thermosetting and Thermoplastic Polymers, Polymer Blends and Composites, Conducting and Biodegradable polymers.",
                        "Preparation, properties, and industrial applications of: Teflon, Lucite, Bakelite, Kelvar, Dacron, Thiokol, Nylon, Buna-N and Buna-S.",
                        "Environmental impact of polymers on society, Speciality polymers.",
                        "Organometallic Compounds: General methods of preparation and applications of Organometallic compounds (CH3MgBr and LiAlH4)."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS103",
            title = "Engineering Mathematics-I",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Matrices",
                    weightage = "20%",
                    topics = listOf(
                        "Elementary transformations, Inverse of a matrix, Rank of matrix.",
                        "Solution of system of linear equations.",
                        "Characteristic equation, Cayley-Hamilton Theorem and its application.",
                        "Linear Dependence and Independence of vectors.",
                        "Eigen values and Eigen vectors.",
                        "Complex Matrices: Hermitian, Skew-Hermitian and Unitary Matrices.",
                        "Applications to Engineering problems."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Differential Calculus-I",
                    weightage = "20%",
                    topics = listOf(
                        "Successive Differentiation (nth order derivatives).",
                        "Leibnitz theorem.",
                        "Curve tracing.",
                        "Partial derivatives.",
                        "Euler’s Theorem for homogeneous functions.",
                        "Total derivative, Change of variables."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Differential Calculus-II",
                    weightage = "20%",
                    topics = listOf(
                        "Expansion of functions by Taylor’s and Maclaurin’s theorems for functions of one and two variables.",
                        "Maxima and Minima of functions of several variables.",
                        "Lagrange’s method of multipliers.",
                        "Jacobians, Approximation of errors."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Multiple integration",
                    weightage = "20%",
                    topics = listOf(
                        "Double integral, Triple integral.",
                        "Change of order of integration, Change of variables.",
                        "Beta and Gamma function and their properties.",
                        "Dirichlet’s integral and its applications to area and volume.",
                        "Liouville’s extensions of Dirichlet’s integral."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Vector Calculus",
                    weightage = "20%",
                    topics = listOf(
                        "Vector differentiation: Gradient, Curl and Divergence and their Physical interpretation, Directional derivatives.",
                        "Vector Integration: Line integral, Surface integral, Volume integral, Gauss’s Divergence theorem, Green’s theorem and Stoke’s theorem (without proof) and their applications."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IEE101",
            title = "Fundamentals of Electrical Engineering",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "DC Circuit Analysis",
                    weightage = "20%",
                    topics = listOf(
                        "Types of Elements and Networks.",
                        "Kirchhoff’s law.",
                        "Ideal and practical voltage and current sources.",
                        "Mesh and Nodal analysis.",
                        "Source transformation, Star delta transformation.",
                        "Superposition theorem, Thevenin’s theorem, Norton’s theorem, Maximum power transfer theorem.",
                        "(Independent and ideal sources based numerical, Source transformation not expected for superposition theorem, Mesh and Nodal analysis.)"
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Analysis of Single-Phase AC Circuits",
                    weightage = "20%",
                    topics = listOf(
                        "Representation of Sinusoidal waveforms – Average and effective values, Form-factor, and peak factors.",
                        "Analysis of single-phase AC Circuits consisting of R-L-C combination (Series and Parallel).",
                        "Apparent, active, reactive power, Power factor.",
                        "Concept of Resonance in series & parallel circuits, bandwidth and quality factor."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Three-Phase AC Circuits, Magnetic Circuits, and Transformers",
                    weightage = "20%",
                    topics = listOf(
                        "Three-phase balanced circuits, voltage and current relations in star and delta connections and related numerical.",
                        "Magnetic Circuits: Concept of MMF, flux, flux density, reluctance, permeability, field strength, and their units.",
                        "Transformers: Principle of working, EMF equation, Ideal and practical transformers, equivalent circuits, losses and efficiency in transformers (Numerical problems related to transformer)."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Electrical machines",
                    weightage = "20%",
                    topics = listOf(
                        "DC Machines: Principles and Construction, EMF equation of Generator.",
                        "Classification of DC Generator: Self-excited, Separately excited, shunt and series generator.",
                        "Principle of DC motor, Torque equation of motor, Series and Shunt motors (simple numerical problems).",
                        "Three-Phase Induction Motor: Principle & Construction and Applications.",
                        "Working principle of Three-Phase Alternator."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Measuring Instruments and Electrical Installations",
                    weightage = "20%",
                    topics = listOf(
                        "Definition and Types of electrical measuring instruments.",
                        "Construction and working principle of PMMC type, MI type and Dynamometer type instruments.",
                        "Electrical Installations: Introduction of Switch Fuse Unit (SFU), MCB, ELCB, MCCB, ACB.",
                        "Types of Wires, Cables. Earthing and its types, Safety Precautions to avoid shock."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IEC101",
            title = "Fundamentals of Electronics Engineering",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Semiconductor Diode and Applications",
                    weightage = "20%",
                    topics = listOf(
                        "Semiconductor Diode: Ideal and practical Diodes V-I characteristics, Diode Equivalent Circuits.",
                        "Zener Diodes breakdown mechanism (Zener and avalanche).",
                        "Diode Applications: Half and Full Wave rectification, Clippers, Clampers.",
                        "Zener diode as shunt regulator, Voltage-Multiplier Circuits.",
                        "Special Purpose two terminal Devices: Light-Emitting Diodes, Photo Diodes, Varactor Diodes, Tunnel Diodes."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Bipolar Junction Transistor and Field Effect Transistor",
                    weightage = "20%",
                    topics = listOf(
                        "Bipolar Junction Transistor: Transistor Construction and Characteristic, Operation, Amplification action.",
                        "Common Base, Common Emitter, Common Collector Configuration.",
                        "Field Effect Transistor: Construction and Characteristic of JFETs, Transfer Characteristic.",
                        "MOSFET (MOS) (Depletion and Enhancement) Type, Transfer Characteristic."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Operational Amplifiers",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction, Op-Amp basic.",
                        "Practical Op-Amp Circuits: Inverting Amplifier, Non-inverting Amplifier, Unit Follower, Summing Amplifier, Integrator, Differentiator."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Digital Electronics",
                    weightage = "20%",
                    topics = listOf(
                        "Number system & representation, Code Conversion, Binary arithmetic.",
                        "Introduction of Basic and Universal Gates.",
                        "Simplification of Boolean function using Boolean algebra.",
                        "K Map Minimisation."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Communication Engineering",
                    weightage = "20%",
                    topics = listOf(
                        "Fundamentals of Communication Engineering: Basics of signal representation and analysis, Electromagnetic spectrum, Elements of a Communication System.",
                        "Need of modulation and typical applications.",
                        "Fundamentals of amplitude modulation and demodulation techniques.",
                        "Introduction to Wireless Communication: Fundamental and Overview of wireless communication and cellular communication.",
                        "Different generations and standards in cellular communication systems."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "ICS101",
            title = "Programming for Problem Solving",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Introduction to Computer System Components and Programming Basics",
                    weightage = "20%",
                    topics = listOf(
                        "Components of a Computer System: Memory, Processor, I/O Devices, Storage, Operating System.",
                        "Concept of Assembler, Compiler, Interpreter, Loader and Linker.",
                        "Idea of Algorithm: Representation of Algorithm, Flowchart, Pseudo Code with Examples.",
                        "From Algorithms to Programs, Source Code.",
                        "Programming Basics: Structure of C Program, Writing and Executing the First C Program, Syntax and Logical Errors in Compilation, Object and Executable Code.",
                        "Components of C Language: Standard I/O in C, Fundamental Data types, Variables and Memory Locations, Storage Classes."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Arithmetic Expressions, Precedence, and Conditional Branching",
                    weightage = "20%",
                    topics = listOf(
                        "Arithmetic Expressions and Precedence: Operators and Expression Using Numeric and Relational Operators, Mixed Operands, Type Conversion, Logical Operators, Bit Operations, Assignment Operator, Operator precedence and Associativity.",
                        "Conditional Branching: Applying if and Switch Statements, Nesting if and Else and Switch."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Iteration, Loops, and Arrays",
                    weightage = "20%",
                    topics = listOf(
                        "Iteration and Loops: Use of While, do While and for Loops, Multiple Loop Variables, Use of Break and Continue Statements.",
                        "Arrays: Array Notation and Representation, Manipulating Array Elements, using Multi Dimensional Array.",
                        "Character Arrays and Strings, Structure, union, Enumerated Data types, Array of Structures, Passing Arrays to Functions."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Functions and Algorithms",
                    weightage = "20%",
                    topics = listOf(
                        "Functions: Introduction, Types of Functions, Functions with Array, Passing Parameters to Functions, Call by Value, Call by Reference, Recursive Functions.",
                        "Basic of searching and Sorting Algorithms: Linear Search, Binary search, Bubble Sort, Insertion and Selection Sort."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Pointers and File Handling",
                    weightage = "20%",
                    topics = listOf(
                        "Pointers: Introduction, Declaration, Applications, Introduction to Dynamic Memory Allocation (Malloc, Calloc, Realloc, Free).",
                        "String and String functions, Use of Pointers in Self-Referential Structures, Notion of Linked List (No Implementation).",
                        "File Handling: File I/O Functions, Standard C Preprocessors, Defining and Calling Macros and Command-Line Arguments."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IME101",
            title = "Fundamentals of Mechanical Engineering",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Introduction to Mechanics of Solid",
                    weightage = "20%",
                    topics = listOf(
                        "Force moment and couple, principle of transmissibility, Varignon's theorem.",
                        "Resultant of force system- concurrent and non-concurrent coplanar forces.",
                        "Types of supports (Hinge, Roller) and loads (Point, UDL, UVL), free body diagram, equilibrium equations and Support Reactions.",
                        "Normal and shear Stress, strain, Hookes’ law, Poisson’s ratio, elastic constants and their relationship.",
                        "Stress-strain diagram for ductile and brittle materials, factor of safety."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Basics of Thermodynamics",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction, microscopic and macroscopic approaches, Concept of continuum, control volume and surfaces.",
                        "Thermodynamic properties, path, process and cycle, thermodynamic equilibrium, Quasistatic process.",
                        "Energy and its forms, work and heat, gas laws, Ideal gas, Zeroth law of thermodynamics.",
                        "First law of thermodynamics: Joules’ experiment, Internal energy and enthalpy, PMM-I.",
                        "Second law of thermodynamics: Heat engines, Efficiency, Heat pump, refrigerator, Kelvin Planck statement, Clausius statement, PMM-II."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Introduction to Fluid Mechanics",
                    weightage = "20%",
                    topics = listOf(
                        "Fluid’s properties: pressure, density, dynamic and kinematic viscosity.",
                        "Surface tension, vapour pressure, cavitation, Newtonian and Non-Newtonian fluid, Pascal’s Law, continuity equation.",
                        "Flow Measurement devices: Simple Manometer, U-Tube manometer, Bourdon tube, Venturi meter, Pitot tube and Orifice meter."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "I.C Engine & Electric Vehicles",
                    weightage = "20%",
                    topics = listOf(
                        "IC Engine: basic definition of engine and components, construction and working of two stroke and four stroke SI & CI engine.",
                        "Merits and demerits, scavenging process; difference between two-stroke and four stroke IC engines and SI and CI Engines.",
                        "Electric vehicles and hybrid vehicles: components of an EV, EV batteries, chargers, drives, transmission and power devices.",
                        "Advantages and disadvantages of EVs. Hybrid electric vehicles, HEV drive train components, advantages of HV."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Introduction to Mechatronics",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction to Mechatronic Systems: Evolution, Scope, advantages and disadvantages, industrial applications.",
                        "Introduction to autotronics, bionics, and avionics and their applications.",
                        "Sensors and transducers: types of sensors, types of transducers and their characteristics.",
                        "Overview of mechanical, hydraulic and pneumatic actuation systems.",
                        "Concept of Measurement, Error in measurements, Calibration.",
                        "Specific sensors: strain (Bonded and Unbonded Strain Gauge), temperature sensor (Thermocouple and Optical Pyrometer), force (Proving Ring) and torques (Prony Brake Dynamometer).",
                        "Concepts of accuracy, precision and resolution."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS104",
            title = "Environment and Ecology",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Environment and Ecosystem",
                    weightage = "20%",
                    topics = listOf(
                        "Environment: Definition, Types of Environment, Components of environment, Segments of environment, Scope and importance, Need for Public Awareness.",
                        "Ecosystem: Definition, Types of ecosystem, Structure of ecosystem, Food Chain, Food Web, Ecological pyramid, Balance Ecosystem.",
                        "Effects of Human Activities: Food, Shelter, Housing, Agriculture, Industry, Mining, Transportation, Economic and Social security on Environment.",
                        "Environmental Impact Assessment, Sustainable Development."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Natural Resources",
                    weightage = "20%",
                    topics = listOf(
                        "Natural Resources: Introduction, Classification.",
                        "Water Resources: Availability, sources and Quality Aspects, Water Borne and Water Induced Diseases, Fluoride and Arsenic Problems in Drinking Water.",
                        "Mineral Resources: Material Cycles; Carbon, Nitrogen and Sulphur cycles.",
                        "Energy Resources: Conventional and Non-conventional Sources of Energy.",
                        "Forest Resources: Availability, Depletion of Forests, Environment impact of forest depletion on society."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Pollution and their Effects",
                    weightage = "20%",
                    topics = listOf(
                        "Public Health Aspects of Environmental.",
                        "Water Pollution, Air Pollution, Soil Pollution, Noise Pollution.",
                        "Solid waste management."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Current Environmental Issues of Importance",
                    weightage = "20%",
                    topics = listOf(
                        "Global Warming, Green House Effects, Climate Change.",
                        "Acid Rain, Ozone Layer Formation and Depletion.",
                        "Population Growth and Automobile pollution, Burning of paddy straw."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Environmental Protection",
                    weightage = "20%",
                    topics = listOf(
                        "Environmental Protection Act 1986.",
                        "Initiatives by Non-Governmental Organisations (NGO’s).",
                        "Human Population and the Environment: Population growth, Environmental Education, Women Education."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS105",
            title = "Soft Skills",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Workplace Communication",
                    weightage = "20%",
                    topics = listOf(
                        "Professional Vocabulary: Antonyms, Synonyms, Homophones, Homonyms.",
                        "Business Correspondence: Letter Agenda, Notices, Minutes of Meeting, CV and Résumé, G.D., Interview.",
                        "Assignments/Activity:",
                        "Group discussion activity using professional vocabulary.",
                        "Task to design a Résumé.",
                        "Task to attend a meeting and write minutes of meeting."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Public Speaking & Presentation Skills",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction to oral communication and Non-verbal Communication.",
                        "Nuances and Modes of Speech Delivery.",
                        "Pre-requisites of Individual Speaking: confidence, clarity, and fluency.",
                        "How to Pitch an idea: Process, Preparation, and Structure.",
                        "Assignments/Activity:",
                        "Presentations on given topics (group or individual)."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Critical Thinking and Emotional Intelligence",
                    weightage = "20%",
                    topics = listOf(
                        "Critical Thinking: Analysis, Interpretation, Inference, Explanation, Self-Regulation, Open-Mindedness and Problem Solving.",
                        "Emotional Intelligence: Self Awareness, Self-regulation, Empathy, Motivation, Social Skills.",
                        "Assignments/Activity:",
                        "Writing a critical assessment or review for case-based studies."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Leadership Skills",
                    weightage = "20%",
                    topics = listOf(
                        "Qualities: Integrity, Capability, Passion.",
                        "Importance of Leadership communication: Aligned Employees with Strategic Goals, Trust, Transparency, Collaborative, Accessible, and Workplace Culture.",
                        "Listening and Responding.",
                        "Assignments/Activity:",
                        "Conducting a Podcast session or interview of an admired personality."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Mental Health and Stress Management",
                    weightage = "20%",
                    topics = listOf(
                        "Mental health at Workplace: Definition and Factors.",
                        "How to Manage Stress: Techniques (Application of 4 A’s: Avoid; Alter; Access; Adapt).",
                        "Value based Reading: A Select Reading.",
                        "Assignments/Activity:",
                        "Poster presentation on the given subjects."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS151",
            title = "Engineering Physics Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS152",
            title = "Engineering Chemistry Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IEE151",
            title = "Basic Electrical Engineering Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IEC151",
            title = "Basic Electronics Engineering Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "ICS151",
            title = "Programming for Problem Solving Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IAS155",
            title = "English Language Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(

            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "ICE151",
            title = "Engineering Graphics & Design Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Introduction to Engineering Drawing and Orthographic Projections",
                    weightage = "20%",
                    topics = listOf(
                        "Principles of Engineering Graphics and their significance.",
                        "Dimensioning, Lettering.",
                        "Scales: Plain, Diagonal and Engineering Scales.",
                        "Orthographic Projection, Projection of Point, Projection of Lines: Projection of straight lines; Projection of lines inclined to one plane and both planes."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Projection of Planes and Solids",
                    weightage = "20%",
                    topics = listOf(
                        "Projection of polygonal surface and circular lamina in first quadrant inclined to one or both reference planes.",
                        "Classification of solids.",
                        "Projection of solids (prisms, pyramids, cylinder, cone) with axis inclined by change of position method."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Sections of Regular Solids and development of Surfaces",
                    weightage = "20%",
                    topics = listOf(
                        "Sections of Solids: Right regular solids and Auxiliary views for true shapes of the sections such as Prism, Cylinder, Pyramid, and Cone.",
                        "Development of surfaces for regular solids (Prism, Cylinder, Pyramid, Cone)."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Isometric Projection",
                    weightage = "20%",
                    topics = listOf(
                        "Isometric scales and projections of simple and combination solids.",
                        "Perspective Projection: Orthographic representation of perspective views — Plane figures and simple solids — Visual Ray Method.",
                        "Conversion of pictorial view into orthographic Projection."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Introduction to Computer Aided Design",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction to AutoCAD: Basic commands for 2D drawing (Line, Circle, Polyline, Rectangle, Hatch, Fillet, Chamfer, Trim, Extend, Offset, Dim style, etc.).",
                        "Transformation of Projections: Conversion of Isometric Views to Orthographic Views and Vice-Versa in AutoCAD.",
                        "Creation of engineering models and presentation in standard 2D blueprint form."
                    )
                )
            )
        ),
        SyllabusCourse(
            semester = 1,
            courseCode = "IWS151",
            title = "Workshop Practice Lab",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(
                    unitNumber = 1,
                    title = "Introduction to Mechanical workshop material, tools and machines",
                    weightage = "20%",
                    topics = listOf(
                        "Study of layout and safety measures.",
                        "Study of engineering materials: mild steel, medium carbon steel, high carbon steel, high speed steel, and cast iron.",
                        "Study and use of tools, equipment, devices, and machines used in fitting, sheet metal, and welding sections.",
                        "Determination of the least count of vernier caliper, vernier height gauge, and micrometer (screw gauge)."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 2,
                    title = "Machine shop",
                    weightage = "20%",
                    topics = listOf(
                        "Demonstration of construction and accessories for Lathe machine.",
                        "Performing operations on Lathe: Facing, Plane Turning, step turning, taper turning, threading, knurling and parting."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 3,
                    title = "Fitting shop",
                    weightage = "20%",
                    topics = listOf(
                        "Practice marking operations.",
                        "Preparation of U or V-shape male-female workpieces using filing, sawing, drilling, and grinding."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 4,
                    title = "Carpentry Shop",
                    weightage = "20%",
                    topics = listOf(
                        "Study of Carpentry tools, equipment, and joints.",
                        "Making of Cross Half lap joint, Half lap Dovetail joint, and Mortise Tenon joint."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 5,
                    title = "Welding Shop",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction to BI standards and reading of welding drawings.",
                        "Practice of Butt Joint, Lap Joint, TIG welding, and MIG welding."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 6,
                    title = "Moulding and Casting Shop",
                    weightage = "20%",
                    topics = listOf(
                        "Introduction to patterns, pattern allowances, and ingredients of moulding sand.",
                        "Foundry tools and purposes; Demonstration of mould preparation and Aluminium casting.",
                        "Study and preparation of plastic mould."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 7,
                    title = "CNC Shop",
                    weightage = "20%",
                    topics = listOf(
                        "Study of main features, working parts, and accessories of CNC machines.",
                        "Performing operations on metal components using CNC machines."
                    )
                ),
                SyllabusUnit(
                    unitNumber = 8,
                    title = "3D Printing",
                    weightage = "20%",
                    topics = listOf(
                        "Preparing a product using 3D printing manufacturing techniques."
                    )
                )
            )
        ),

        // ----------------------------------------------------
        // SEMESTER II SYLLABUS DATA (Verified against iet-btech-sem2-syllabus-v2.pdf)
        // ----------------------------------------------------
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS201",
            title = "Engineering Physics",
            dept = "",
            credits = 4,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Quantum Mechanics", "20%", listOf(
                    "Inadequacy of classical mechanics, Planck’s theory of black body radiation (qualitative), Compton effect.",
                    "de-Broglie concept of matter waves, Davisson and Germer Experiment.",
                    "Phase velocity and group velocity, Time-dependent and time-independent Schrodinger wave equations.",
                    "Physical interpretation of wave function, Particle in a one-Dimensional box."
                )),
                SyllabusUnit(2, "Electromagnetic Field Theory", "20%", listOf(
                    "Basic concept of Stoke’s theorem and Divergence theorem, Basic laws of electricity and magnetism.",
                    "Continuity equation for current density, Displacement current.",
                    "Maxwell equations in integral and differential form, Maxwell equations in vacuum and in conducting medium.",
                    "Poynting vector and Poynting theorem, Plane electromagnetic waves in vacuum and their transverse nature.",
                    "Relation between electric and magnetic fields of an electromagnetic wave, Plane electromagnetic waves in conducting medium, Skin depth."
                )),
                SyllabusUnit(3, "Wave Optics", "20%", listOf(
                    "Coherent sources, Interference in uniform and wedge shaped thin films, Colours of thin films, Necessity of extended sources.",
                    "Newton’s Rings and its applications (Wavelength and refractive index).",
                    "Introduction to diffraction, Fraunhoffer diffraction at single slit and double slit, Absent spectra.",
                    "Diffraction grating, Spectra with grating, Maximum numbers of orders with grating, Absent spectra with grating, Dispersive power of grating."
                )),
                SyllabusUnit(4, "Fiber Optics & Laser", "20%", listOf(
                    "Fibre Optics: Principle and construction of optical fiber, Acceptance angle, Numerical aperture, Acceptance cone, Step index and graded index fibers, Fiber optic communication principle, Attenuation, Application of fiber.",
                    "Laser: Absorption of radiation, Spontaneous and stimulated emission of radiation, Population inversion, Einstein’s Coefficients, Principles of laser action, Solid state Laser (Ruby laser) and Gas Laser (He-Ne laser), Laser applications."
                )),
                SyllabusUnit(5, "Superconductors and Nano-Materials", "20%", listOf(
                    "Superconductors: Temperature dependence of resistivity in superconducting materials, Meissner effect, Temperature dependence of critical field, Persistent current, Type I and Type II superconductors, Properties and Applications of Superconductors.",
                    "Nano-Materials: Introduction and properties of nano materials, Basic concept of Quantum Dots, Quantum wires and Quantum well, Fabrication of nano materials - Top-Down approach (CVD) and Bottom-Up approach (Sol Gel), Properties and Applications of nano materials."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS202",
            title = "Engineering Chemistry",
            dept = "",
            credits = 4,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Solid State, Advanced Materials & Green Chemistry", "20%", listOf(
                    "Solid State Chemistry: Introduction, Crystal Imperfections; Stoichiometric Defects and Nonstoichiometric Defects.",
                    "Chemistry of Advanced Materials: Liquid Crystals (Introduction, Types and Applications, Industrially important materials used as liquid crystals); Graphite and Fullerene (Introduction, Structure and applications); Nanomaterials (Introduction, Preparation, characteristics, applications, Carbon Nano Tubes (CNT)).",
                    "Green Chemistry: Introduction, 12 principles and importance of green Synthesis, Green Chemicals, Synthesis of typical organic compounds by conventional and Green route (Adipic acid and Paracetamol), Environmental impact of Green chemistry on society."
                )),
                SyllabusUnit(2, "Spectroscopic Techniques and Applications & Stereochemistry", "20%", listOf(
                    "Spectroscopic Techniques and Applications: Elementary idea and simple applications of UV, IR and NMR, Numerical problems.",
                    "Stereochemistry: Optical isomerism in compounds without chiral carbon, Geometrical isomerism, Chiral Drugs."
                )),
                SyllabusUnit(3, "Named Reactions, Catalysis, Corrosion & Engineering Materials", "20%", listOf(
                    "Named Reactions & Mechanisms: Beckmann Reaction, Diel’s Alder Reaction, Cannizzaro Reaction, Aldol Condensation, Reimer Tiemann reaction, Hofmann Reaction and Friedel Craft Reaction.",
                    "Catalysis: Introduction, Characteristics of catalyst, Types of catalyst, Theories of catalysis, Positive and Negative catalyst, Catalytic promoters and Inhibitors, Autocatalyst, Enzyme catalyst and Industrial catalyst.",
                    "Corrosion: Introduction, Types, Cause, Prevention and control, Corrosion issues in specific industries (Power generation, Chemical processing industry, Oil & gas industry and Pulp & paper industries).",
                    "Chemistry of Engineering Materials: Cement (Constituents, manufacturing, hardening and setting, deterioration), Plaster of Paris (POP)."
                )),
                SyllabusUnit(4, "Water Technology & Fuels and Combustion", "20%", listOf(
                    "Water Technology: Sources and impurities of water, Hardness of water, Boiler troubles, Techniques for water softening (Lime-Soda, Zeolite, Ion Exchange and Reverse Osmosis process), Determination of Hardness and alkalinity, Numerical problems.",
                    "Fuels and Combustion: Definition, Classification, Characteristics of a good fuel, Calorific Values, Gross & Net calorific value, Determination of calorific value by Bomb Calorimeter, Theoretical calculation of calorific value by Dulong’s method, Ranking of Coal, Analysis of coal by Proximate and Ultimate analysis method, Numerical problems, Chemistry of Biogas production from organic waste materials and their environmental impact on society."
                )),
                SyllabusUnit(5, "Materials Chemistry & Organometallic Compounds", "20%", listOf(
                    "Materials Chemistry: Polymers (Classification, Polymerization processes, Thermosetting and Thermoplastic Polymers, Polymer Blends and Composites, Conducting and Biodegradable polymers, Speciality polymers).",
                    "Industrial applications of Teflon, Lucite, Bakelite, Kevlar, Dacron, Thiokol, Nylon, Buna-N and Buna-S, and their environmental impact on society.",
                    "Organometallic Compounds: General methods of preparation and applications of Organometallic compounds (CH3MgBr and LiAlH4)."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS203",
            title = "Engineering Mathematics-II",
            dept = "",
            credits = 4,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Ordinary Differential Equation of Higher Order", "20%", listOf(
                    "Linear differential equation of nth order with constant coefficients, Simultaneous linear differential equations.",
                    "Second order linear differential equations with variable coefficients, Solution by changing independent variable.",
                    "Method of variation of parameters, Cauchy-Euler equation, Application of differential equations in solving engineering problems."
                )),
                SyllabusUnit(2, "Laplace Transform", "20%", listOf(
                    "Laplace transform, Existence theorem, Properties of Laplace Transform, Laplace transform of derivatives and integrals.",
                    "Unit step function, Laplace transform of periodic function, Inverse Laplace transform, Convolution theorem.",
                    "Application of Laplace Transform to solve ordinary differential equations and simultaneous differential equations."
                )),
                SyllabusUnit(3, "Sequence and Series", "20%", listOf(
                    "Definition of Sequence and series with examples, Convergence of series, Tests for convergence of series (Ratio test, D’Alembert’s test, Raabe’s test, Comparison test).",
                    "Fourier series, Half range Fourier sine and cosine series."
                )),
                SyllabusUnit(4, "Complex Variable – Differentiation", "20%", listOf(
                    "Functions of complex variable, Limit, Continuity and differentiability, Analytic functions.",
                    "Cauchy-Riemann equations (Cartesian and Polar form), Harmonic function, Method to find Analytic functions (Milne’s Thompson Method).",
                    "Conformal mapping, Mobius transformation and their properties."
                )),
                SyllabusUnit(5, "Complex Variable – Integration", "20%", listOf(
                    "Complex integration, Cauchy-Integral theorem, Cauchy integral formula.",
                    "Taylor’s and Laurent’s series, singularities and its classification, zeros of analytic functions, Residues, Cauchy’s Residue theorem and its application."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IEE201",
            title = "Fundamentals of Electrical Engineering",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "DC Circuit Analysis", "20%", listOf(
                    "Types of Elements and Networks, Kirchhoff’s laws (KVL/KCL), Ideal and practical voltage and current sources.",
                    "Mesh and Nodal analysis, Source transformation, Star delta transformation.",
                    "Superposition theorem, Thevenin’s theorem, Norton’s theorem, Maximum power transfer theorem (Independent and ideal sources based numericals)."
                )),
                SyllabusUnit(2, "Analysis of Single-Phase AC Circuits", "20%", listOf(
                    "Representation of Sinusoidal waveforms – Average and effective values, Form-factor, and peak factors.",
                    "Analysis of single-phase AC Circuits consisting of R-L-C combination (Series and Parallel).",
                    "Apparent, active, reactive power, Power factor.",
                    "Concept of Resonance in series & parallel circuits, bandwidth and quality factor."
                )),
                SyllabusUnit(3, "Three-Phase AC Circuits, Magnetic Circuits & Transformers", "20%", listOf(
                    "Three-Phase AC Circuits: Three-phase balanced circuits, voltage and current relations in star and delta connections and related numericals.",
                    "Magnetic Circuits: Concept of MMF, flux, flux density, reluctance, permeability, field strength, and their units.",
                    "Transformers: Principle of working, EMF equation, Ideal and practical transformers, equivalent circuits, losses and efficiency in transformers (Numerical problems related to transformer)."
                )),
                SyllabusUnit(4, "Electrical Machines", "20%", listOf(
                    "DC Machines: Principles and Construction, EMF equation of Generator, Classification of DC Generator: Self-excited, Separately excited, shunt and series generator.",
                    "Principle of DC motor, Torque equation of motor, Series and Shunt motors (simple numerical problems).",
                    "Three-Phase Induction Motor: Principle, Construction and Applications.",
                    "Three-Phase Alternator: Working principle."
                )),
                SyllabusUnit(5, "Measuring Instruments & Electrical Installations", "20%", listOf(
                    "Measuring Instruments: Definition and Types of electrical measuring instruments, Construction and working principle of PMMC type, MI type and Dynamometer type instruments.",
                    "Electrical Installations: Introduction of Switch Fuse Unit (SFU), MCB, ELCB, MCCB, ACB, Types of Wires, Cables, Earthing and its types, Safety Precautions to avoid shock."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IEC201",
            title = "Fundamentals of Electronics Engineering",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Semiconductor Diode & Applications", "20%", listOf(
                    "Semiconductor Diode: Ideal and practical Diodes, V-I characteristics, Diode Equivalent Circuits, Zener Diodes breakdown mechanism (Zener and avalanche).",
                    "Diode Application: Diode Applications, Half and Full Wave rectification, Clippers, Clampers, Zener diode as shunt regulator, Voltage-Multiplier Circuits.",
                    "Special Purpose Two-Terminal Devices: Light-Emitting Diodes, Photo Diodes, Varactor Diodes, Tunnel Diodes."
                )),
                SyllabusUnit(2, "Bipolar Junction Transistor & Field Effect Transistor", "20%", listOf(
                    "Bipolar Junction Transistor: Transistor Construction and Characteristic, Operation, Amplification action, Common Base, Common Emitter, Common Collector Configuration.",
                    "Field Effect Transistor: Construction and Characteristic of JFETs, Transfer Characteristic, MOSFET (MOS) (Depletion and Enhancement Type), Transfer Characteristic."
                )),
                SyllabusUnit(3, "Operational Amplifiers", "20%", listOf(
                    "Operational Amplifiers: Introduction, Op-Amp basics, Practical Op-Amp Circuits: Inverting Amplifier, Non-inverting Amplifier, Unit Follower, Summing Amplifier, Integrator, Differentiator."
                )),
                SyllabusUnit(4, "Digital Electronics", "20%", listOf(
                    "Digital Electronics: Number system & representation, Code Conversion, Binary arithmetic, Introduction of Basic and Universal Gates, Using Boolean algebra simplification of Boolean function, K-Map Minimization."
                )),
                SyllabusUnit(5, "Fundamentals of Communication Engineering & Wireless Communication", "20%", listOf(
                    "Fundamentals of Communication Engineering: Basics of signal representation and analysis, Electromagnetic spectrum, Elements of a Communication System, Need of modulation and typical applications, Fundamentals of amplitude modulation and demodulation techniques.",
                    "Introduction to Wireless Communication: Fundamental and Overview of wireless communication and cellular communication, Different generations and standards in cellular communication systems."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "ICS201",
            title = "Programming for Problem Solving",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Computer Basics & Programming Concepts", "20%", listOf(
                    "Introduction to Components of a Computer System: Memory, Processor, I/O Devices, Storage, Operating System, Concept of Assembler, Compiler, Interpreter, Loader and Linker.",
                    "Idea of Algorithm: Representation of Algorithm, Flowchart, Pseudo Code with Examples, From Algorithms to Programs, Source Code.",
                    "Programming Basics: Structure of C Program, Writing and Executing the First C Program, Syntax and Logical Errors in Compilation, Object and Executable Code, Components of C Language, Standard I/O in C, Fundamental Data types, Variables and Memory Locations, Storage Classes."
                )),
                SyllabusUnit(2, "Expressions, Operators & Conditional Branching", "20%", listOf(
                    "Arithmetic Expressions and Precedence: Operators and Expression Using Numeric and Relational Operators, Mixed Operands, Type Conversion, Logical Operators, Bit Operations, Assignment Operator, Operator precedence and Associativity.",
                    "Conditional Branching: Applying if and Switch Statements, Nesting if and Else and Switch."
                )),
                SyllabusUnit(3, "Loops, Arrays & Structures", "20%", listOf(
                    "Iteration and Loops: Use of While, do While and for Loops, Multiple Loop Variables, Use of Break and Continue Statements.",
                    "Arrays: Array Notation and Representation, Manipulating Array Elements, using Multi Dimensional Array, Character Arrays and Strings, Structure, union, Enumerated Data types, Array of Structures, Passing Arrays to Functions."
                )),
                SyllabusUnit(4, "Functions & Searching/Sorting Algorithms", "20%", listOf(
                    "Functions: Introduction, Types of Functions, Functions with Array, Passing Parameters to Functions, Call by Value, Call by Reference, Recursive Functions.",
                    "Basic of Searching and Sorting Algorithms: Searching & Sorting Algorithms (Linear Search, Binary search, Bubble Sort, Insertion and Selection Sort)."
                )),
                SyllabusUnit(5, "Pointers & File Handling", "20%", listOf(
                    "Pointers: Introduction, Declaration, Applications, Introduction to Dynamic Memory Allocation (Malloc, Calloc, Realloc, Free), String and String functions, Use of Pointers in Self-Referential Structures, Notion of Linked List (No Implementation).",
                    "File Handling: File I/O Functions, Standard C Preprocessors, Defining and Calling Macros and Command-Line Arguments."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IME201",
            title = "Fundamentals of Mechanical Engineering",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Introduction to Mechanics of Solids", "20%", listOf(
                    "Force moment and couple, principle of transmissibility, Varignon's theorem, Resultant of force system- concurrent and non-concurrent coplanar forces.",
                    "Types of supports (Hinge, Roller) and loads (Point, UDL, UVL), free body diagram, equilibrium equations and Support Reactions.",
                    "Normal and shear Stress, strain, Hookes’ law, Poisson’s ratio, elastic constants and their relationship, stress-strain diagram for ductile and brittle materials, factor of safety."
                )),
                SyllabusUnit(2, "Basics of Thermodynamics", "20%", listOf(
                    "Introduction, microscopic and macroscopic approaches, Concept of continuum, control volume and surfaces, thermodynamic properties, path, process and cycle, thermodynamic equilibrium, Quasistatic process, Energy and its forms, work and heat, gas laws, Ideal gas, Zeroth law of thermodynamics.",
                    "First law of thermodynamics: Joules’ experiment, Internal energy and enthalpy, PMM-I.",
                    "Second law of thermodynamics: Heat engines, Efficiency, Heat pump, refrigerator, Kelvin Planck statement, Clausius statement, PMM-II."
                )),
                SyllabusUnit(3, "Introduction to Fluid Mechanics & Flow Measurement", "20%", listOf(
                    "Fluid’s properties: pressure, density, dynamic and kinematic viscosity, Surface tension, vapor pressure, cavitation, Newtonian and Non-Newtonian fluid, Pascal’s Law, continuity equation.",
                    "Flow Measurement devices: Simple Manometer, U-Tube manometer, Bourdon tube, Venturi meter, Pitot tube and Orifice meter."
                )),
                SyllabusUnit(4, "I.C. Engine & Electric Vehicles", "20%", listOf(
                    "IC Engine: basic definition of engine and components, construction and working of two stroke and four stroke SI & CI engine, merits and demerits, scavenging process, difference between two-stroke and four stroke IC engines and SI and CI Engines.",
                    "Electric vehicles and hybrid vehicles: components of an EV, EV batteries, chargers, drives, transmission and power devices, Advantages and disadvantages of EVs, Hybrid electric vehicles, HEV drive train components, advantages of HV."
                )),
                SyllabusUnit(5, "Introduction to Mechatronics & Measurements", "20%", listOf(
                    "Introduction to Mechatronic Systems: Evolution, Scope, advantages and disadvantages, industrial applications, introduction to autotronics, bionics, and avionics and their applications.",
                    "Sensors and transducers: types of sensors, types of transducers and their characteristics, Overview of mechanical, hydraulic and pneumatic actuation systems.",
                    "Concept of Measurement: Error in measurements, Calibration, strain (Bonded and Unbonded Strain Gauge), temperature sensor (Thermocouple and Optical Pyrometer), force (Proving Ring) and torques (Prony Brake Dynamometer), Concepts of accuracy, precision and resolution."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS204",
            title = "Environment and Ecology",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Environment & Ecosystem", "20%", listOf(
                    "Environment: Definition, Types of Environment, Components of environment, Segments of environment, Scope and importance, Need for Public Awareness.",
                    "Ecosystem: Definition, Types of ecosystem, Structure of ecosystem, Food Chain, Food Web, Ecological pyramid, Balanced Ecosystem.",
                    "Effects of Human Activities: Effects of Food, Shelter, Housing, Agriculture, Industry, Mining, Transportation, Economic and Social security on Environment, Environmental Impact Assessment, Sustainable Development."
                )),
                SyllabusUnit(2, "Natural Resources", "20%", listOf(
                    "Natural Resources: Introduction, Classification.",
                    "Water Resources: Availability, sources and Quality Aspects, Water Borne and Water Induced Diseases, Fluoride and Arsenic Problems in Drinking Water.",
                    "Mineral Resources & Material Cycles: Material Cycles (Carbon, Nitrogen and Sulfur cycles).",
                    "Energy Resources: Conventional and Non-conventional Sources of Energy.",
                    "Forest Resources: Availability, Depletion of Forests, Environmental impact of forest depletion on society."
                )),
                SyllabusUnit(3, "Environmental Pollution", "20%", listOf(
                    "Pollution and their Effects: Public Health Aspects of Environmental, Water Pollution, Air Pollution, Soil Pollution, Noise Pollution, Solid waste management."
                )),
                SyllabusUnit(4, "Current Environmental Issues", "20%", listOf(
                    "Current Environmental Issues of Importance: Global Warming, Green House Effects, Climate Change, Acid Rain, Ozone Layer Formation and Depletion, Population Growth and Automobile pollution, Burning of paddy straw."
                )),
                SyllabusUnit(5, "Environmental Protection & Population", "20%", listOf(
                    "Environmental Protection: Environmental Protection Act 1986, Initiatives by Non-Governmental Organizations (NGO’s).",
                    "Human Population and the Environment: Population growth, Environmental Education, Women Education."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS205",
            title = "Soft Skills",
            dept = "",
            credits = 3,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Work Place Communication", "20%", listOf(
                    "Professional Vocabulary: Antonyms, Synonyms, Homophones, Homonyms.",
                    "Business Correspondence: Letter, Agenda, Notices, Minutes of Meeting, CV and Résumé, Group Discussion (G.D.), Interview."
                )),
                SyllabusUnit(2, "Public Speaking & Presentation Skills", "20%", listOf(
                    "Oral and Non-verbal Communication: Nuances and Modes of Speech Delivery.",
                    "Pre-requisites of Individual Speaking: Confidence, clarity, and fluency.",
                    "How to Pitch an Idea: Process, Preparation, and Structure."
                )),
                SyllabusUnit(3, "Critical Thinking and Emotional Intelligence", "20%", listOf(
                    "Critical Thinking: Analysis, Interpretation, Inference, Explanation, Self-Regulation, Open-Mindedness and Problem Solving.",
                    "Emotional Intelligence: Self-Awareness, Self-regulation, Empathy, Motivation, Social Skills."
                )),
                SyllabusUnit(4, "Leadership Skills", "20%", listOf(
                    "Qualities of Leadership: Integrity, Capability, Passion.",
                    "Leadership Communication: Aligning Employees with Strategic Goals, Trust, Transparency, Collaborative, Accessible, and Workplace Culture, Listening and Responding."
                )),
                SyllabusUnit(5, "Mental Health and Stress Management", "20%", listOf(
                    "Mental Health at Workplace: Definition and Factors.",
                    "How to Manage Stress: Techniques (Application of 4 A’s: Avoid, Alter, Accept/Access, Adapt).",
                    "Value-Based Reading: A Select Reading."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS251",
            title = "Engineering Physics Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Engineering Physics Lab - Group A", "50%", listOf(
                    "To determine the wavelength of sodium light by Newton’s ring experiment.",
                    "To determine the wavelength of different spectral lines of mercury light using plane transmission grating.",
                    "To determine the specific rotation of cane sugar solution using polarimeter.",
                    "To determine the focal length of the combination of two lenses separated by a distance and verify the formula for the focal length of combination of lenses.",
                    "To measure attenuation in an optical fiber.",
                    "To determine the wavelength of He-Ne laser light using single slit diffraction.",
                    "To study the polarization of light using He-Ne laser light.",
                    "To determine the wavelength of sodium light with the help of Fresnel’s bi-prism.",
                    "To determine the coefficient of viscosity of a given liquid.",
                    "To determine the value of acceleration due to gravity (g) using compound pendulum."
                )),
                SyllabusUnit(2, "Engineering Physics Lab - Group B", "50%", listOf(
                    "To determine the energy band gap of a given semiconductor material.",
                    "To study Hall effect and determine Hall coefficient, carrier density and mobility of a given semiconductor material using Hall effect setup.",
                    "To determine the variation of magnetic field with the distance along the axis of a current carrying coil and estimate the radius of the coil.",
                    "To verify Stefan’s law by electric method.",
                    "To determine resistance per unit length and specific resistance of a given resistance using Carey Foster's Bridge.",
                    "To study the resonance condition of a series LCR circuit.",
                    "To determine the electrochemical equivalent (ECE) of copper.",
                    "To calibrate the given ammeter and voltmeter by potentiometer.",
                    "To draw hysteresis (B-H curve) of a specimen in the form of a transformer and to determine its hysteresis loss.",
                    "To measure high resistance by leakage method."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS252",
            title = "Engineering Chemistry Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Engineering Chemistry Lab", "100%", listOf(
                    "Calibration of Analytical Equipment and apparatus.",
                    "Determination of Hardness of water sample by EDTA method.",
                    "Determination of Alkalinity of water sample.",
                    "Determination of pH by titrimetric method.",
                    "Determination of surface tension of given liquid.",
                    "Determination of Viscosity of a given liquid by viscometer.",
                    "Determination of the strength of Ferrous ammonium sulfate using external indicator.",
                    "Determination of the strength of Potassium dichromate using internal indicator.",
                    "Determination of available chlorine in bleaching powder.",
                    "Determination of chloride content in water sample.",
                    "Preparation of Phenol formaldehyde (PF) resin.",
                    "Preparation of Urea formaldehyde (UF) resin.",
                    "Preparation of Adipic acid / Paracetamol.",
                    "Determination of Cell Conductance of a solution.",
                    "Determination of Rate constant of hydrolysis of esters.",
                    "Element detection and identification of functional groups in organic compounds."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IEE251",
            title = "Basic Electrical Engineering Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Hardware-Based Experiments", "100%", listOf(
                    "Verification of KCL and KVL.",
                    "Verification of Thevenin’s theorem and Norton’s theorem.",
                    "Verification of Superposition theorem.",
                    "Measurement of power and power factor in a single phase ac series inductive circuit.",
                    "Study of phenomenon of resonance in RLC series circuit and obtain resonant frequency.",
                    "Connection and measurement of power consumption of a fluorescent lamp (tube light).",
                    "Measurement of power in 3-phase circuit by two-wattmeter method and determination of its power factor for star and/or delta connected load.",
                    "Determination of parameters of ac single phase series RLC circuit.",
                    "Determination of the Voltage ratio and polarity test of a single-Phase Transformer.",
                    "Demonstration of cut-out sections of machines: dc machine, single-phase induction machine."
                )),
                SyllabusUnit(2, "Virtual Lab Experiments", "100%", listOf(
                    "Kirchhoff’s laws (http://vlab.amrita.edu/?sub=3&brch=75&sim=217&cnt=2).",
                    "Thevenin Theorem (https://vlab.amrita.edu/?sub=1&brch=75&sim=313&cnt=1).",
                    "RLC series resonance (https://vlab.amrita.edu/?sub=1&brch=75&sim=330&cnt=1).",
                    "Determination of parameters of ac single phase series RLC circuit (https://vlab.amrita.edu/?sub=1&brch=75&sim=332&cnt=1)."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IEC251",
            title = "Basic Electronics Engineering Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "PCB and Soldering Lab", "50%", listOf(
                    "Study of various types of Active & Passive Components based on their ratings.",
                    "Identification of various types of Printed Circuit Boards (PCB) and soldering Techniques.",
                    "PCB Lab: Artwork & printing of a simple PCB; Etching & drilling of PCB.",
                    "Soldering shop: Soldering and desoldering of Resistor, IC, and Capacitor in PCB."
                )),
                SyllabusUnit(2, "Suggestive List of Experiments", "50%", listOf(
                    "Study of Lab Equipment: CRO, Multimeter, and Function Generator, Power supply.",
                    "Study of Components Active, Passive Components and Bread Board.",
                    "P-N Junction diode: Characteristics of PN Junction diode - Static and dynamic resistance measurement from graph.",
                    "Applications of PN Junction diode: Half & Full wave rectifier - Measurement of Vrms, Vdc, and ripple factor.",
                    "Characteristics of Zener diode: V-I characteristics of zener diode, Graphical measurement of forward and reverse resistance.",
                    "Characteristic of BJT: BJT in CE configuration.",
                    "Characteristic of FET: FET in CS configuration.",
                    "To study Operational Amplifier as Adder and Subtractor.",
                    "Verification of Truth Table of Various Logic Gates.",
                    "Implementation of the given Boolean function using logic gates in both SOP and POS forms."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "ICS251",
            title = "Programming for Problem Solving Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Programming Assignments", "100%", listOf(
                    "WAP that accepts the marks of 5 subjects and finds the sum and percentage marks obtained by the student.",
                    "WAP that calculates the Simple Interest and Compound Interest (Principal, Amount, Rate, and Time are keyboard inputs).",
                    "WAP to calculate the area and circumference of a circle.",
                    "WAP that accepts the temperature in Centigrade and converts into Fahrenheit using C/5=(F-32)/9.",
                    "WAP that swaps values of two variables using a third variable.",
                    "WAP that checks whether the two numbers entered by the user are equal or not.",
                    "WAP to find the greatest of three numbers.",
                    "WAP that finds whether a given number is even or odd.",
                    "WAP that tells whether a given year is a leap year or not.",
                    "WAP that accepts marks of five subjects and finds percentage and prints grades: 90-100% (A), 80-90% (B), 60-80% (C), <60% (D).",
                    "WAP that takes two operands and one operator from the user, performs the operation, and prints the result using Switch statement.",
                    "WAP to print the sum of all numbers up to a given number.",
                    "WAP to find the factorial of a given number.",
                    "WAP to print sum of even and odd numbers from 1 to N numbers.",
                    "WAP to print the Fibonacci series.",
                    "WAP to check whether the entered number is prime or not.",
                    "WAP to find the sum of digits of the entered number.",
                    "WAP to find the reverse of a number.",
                    "WAP to print Armstrong numbers from 1 to 100.",
                    "WAP to convert binary number into decimal number and vice versa.",
                    "WAP that simply takes elements of the array from the user and finds the sum of these elements.",
                    "WAP that inputs two arrays and saves sum of corresponding elements of these arrays in a third array and prints them.",
                    "WAP to find the minimum and maximum element of the array.",
                    "WAP to search an element in an array using Linear Search.",
                    "WAP to sort the elements of the array in ascending order using Bubble Sort technique.",
                    "WAP to add and multiply two matrices of order nxn.",
                    "WAP that finds the sum of diagonal elements of a mxn matrix.",
                    "WAP to implement strlen(), strcat(), strcpy() using the concept of Functions.",
                    "Define a structure data type TRAIN_INFO (Train No, Train name, Departure Time, Arrival Time, Start station, End station). Maintain a train timetable and implement query operations.",
                    "WAP to swap two elements using the concept of pointers.",
                    "WAP to compare the contents of two files and determine whether they are same or not.",
                    "WAP to check whether a given word exists in a file or not. If yes, then find the number of times it occurs."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IAS255",
            title = "English Language Lab",
            dept = "",
            credits = 1,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Interactive Communication Skills (Practicals)", "50%", listOf(
                    "Group Discussion: Practical based on Accurate and Current Grammatical Patterns.",
                    "Conversational Skills for Interviews under suitable Professional Communication Lab conditions with emphasis on Kinesics.",
                    "Communication Skills for Seminars/Conferences/Workshops with emphasis on Paralinguistic/Kinesics.",
                    "Presentation Skills for Technical Paper/Project Reports/Proposals based on proper Stress and Intonation Mechanics.",
                    "Official/Public Speaking practice sessions based on suitable Rhythmic Patterns.",
                    "Theme Presentation/Keynote Presentation based on correct methodologies of argumentation.",
                    "Individual Speech Delivery/Conferencing with skills to defend Interjections/Quizzes.",
                    "Argumentative Skills/Role Play Presentation with Stress and Intonation.",
                    "Comprehension Skills based on Reading and Listening Practicals on a model Audio.",
                    "Startup presentations, Video portfolio, Extempore, Role play, Just a Minute (JAM) etc."
                )),
                SyllabusUnit(2, "Computer Assisted Software Based Language Learning", "50%", listOf(
                    "Software-based self-guided learning to provide required English language proficiency from an employability and career readiness standpoint.",
                    "Aligns to Common European Framework of Reference for Languages (CEFR) and delivers a CEFR level - B2 upon completion."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "ICE251",
            title = "Engineering Graphics & Design Lab",
            dept = "",
            credits = 2,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Introduction to Engineering Drawing and Orthographic Projections", "20%", listOf(
                    "Significance, Dimensioning, Lettering, Scales (Plain, Diagonal, Engineering), Projection of Points, Projection of Lines (inclined to one or both planes)."
                )),
                SyllabusUnit(2, "Projection of Planes and Solids", "20%", listOf(
                    "Projection of polygonal surfaces and circular lamina in first quadrant, classification of solids, Projection of solids (prisms, pyramids, cylinder, cone) when axis is inclined by change of position method."
                )),
                SyllabusUnit(3, "Sections of Regular Solids and Development of Surfaces", "20%", listOf(
                    "Sectioning right regular solids (Prism, Cylinder, Pyramid, Cone) and auxiliary views for true shape.",
                    "Development of surfaces of regular solids."
                )),
                SyllabusUnit(4, "Isometric Projection", "20%", listOf(
                    "Isometric scales, isometric projections of simple and combination of solids.",
                    "Perspective Projection (orthographic representation, plane figures, visual ray method, converting pictorial view to orthographic view)."
                )),
                SyllabusUnit(5, "Introduction to Computer Aided Design (AutoCAD)", "20%", listOf(
                    "Basic 2D commands (Line, Circle, Polyline, Rectangle, Hatch, Fillet, Chamfer, Trim, Extend, Offset, Dim style, etc.).",
                    "Converting Isometric Views to Orthographic Views and vice-versa in AutoCAD, 2D blueprints."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IWS251",
            title = "Workshop Practice Lab",
            dept = "",
            credits = 2,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Introduction to Mechanical Workshop Material, Tools and Machines", "12.5%", listOf(
                    "Safety, engineering materials (mild steel, carbon steels, cast iron, etc.), fitting, sheet metal, and welding tools.",
                    "Least count of vernier caliper, height gauge, micrometer."
                )),
                SyllabusUnit(2, "Machine Shop", "12.5%", listOf(
                    "Construction, accessories and operations on Lathe (Facing, Plane Turning, step turning, taper turning, threading, knurling, parting)."
                )),
                SyllabusUnit(3, "Fitting Shop", "12.5%", listOf(
                    "Practice marking, and preparing U or V-shape Male-Female workpieces (Filing, Sawing, Drilling, Grinding)."
                )),
                SyllabusUnit(4, "Carpentry Shop", "12.5%", listOf(
                    "Tools, equipment, and wood joints (Cross Half lap, Half lap Dovetail, and Mortise Tenon joints)."
                )),
                SyllabusUnit(5, "Welding Shop", "12.5%", listOf(
                    "BI standards and making Butt joints, Lap joints, TIG, and MIG welding."
                )),
                SyllabusUnit(6, "Moulding and Casting Shop", "12.5%", listOf(
                    "Patterns, allowances, moulding sand, foundry tools, and melting furnaces.",
                    "Mould prep and Aluminum/Plastic casting demos."
                )),
                SyllabusUnit(7, "CNC Shop", "12.5%", listOf(
                    "Main features and parts of CNC machine. Performing operations on metal components."
                )),
                SyllabusUnit(8, "3D Printing", "12.5%", listOf(
                    "Preparing a functional engineered product using 3D printing manufacturing technique."
                ))
            )
        ),
        SyllabusCourse(
            semester = 2,
            courseCode = "IVA251",
            title = "Sports and Yoga",
            dept = "",
            credits = 0,
            ncrfHours = 0,
            units = listOf(
                SyllabusUnit(1, "Sports/Games", "50%", listOf(
                    "Athletics: Compulsory form of physical training and field events.",
                    "Elective Games (Opt at least one): Volleyball, Basketball, Handball, Football, Badminton, Kabaddi, Kho-kho, Table Tennis, or Cricket.",
                    "Field practice, field dimensions, rules, regulations, terminology, and tournament systems."
                )),
                SyllabusUnit(2, "Yoga", "50%", listOf(
                    "Introduction of Yoga: Origin, Aims and Objectives, Patanjal Yoga darshan, Hathyoga, Gheranda Samhita, Karmyoga, and Gyanyoga.",
                    "Asanas, Pranayam, and Meditation Practices: Yogic postures (Standing, Sitting, Supine, Prone, and balancing postures), Pranayam according to Patanjali and Hath Yoga, Meditation Mudras.",
                    "Science of Yoga: Physiological effects of Asanas, Pranayama and Meditation; Stress management, mental health, and personality development."
                ))
            )
        )
    )

    // ----------------------------------------------------
    // OFFICIAL COLLEGE HOLIDAYS 2026 (Extracted from official document)
    // ----------------------------------------------------
    val OFFICIAL_HOLIDAYS_2026: List<OfficialHoliday> = listOf(
        OfficialHoliday("h1", "Birth Anniversary of Maulana Hazrat Ali", "2026-01-03", "2026-01-03", "Saturday", 1, "", "Religious"),
        OfficialHoliday("h2", "Republic Day", "2026-01-26", "2026-01-26", "Monday", 1, "", "National"),
        OfficialHoliday("h3", "Maha Shivaratri", "2026-02-15", "2026-02-15", "Sunday", 1, "", "Religious"),
        OfficialHoliday("h4", "Holika Dahan", "2026-03-02", "2026-03-02", "Monday", 1, "", "Festival"),
        OfficialHoliday("h5", "Holi", "2026-03-04", "2026-03-04", "Wednesday", 1, "", "Festival"),
        OfficialHoliday("h6", "Holi (Additional Day)", "2026-03-05", "2026-03-05", "Thursday", 1, "", "Festival"),
        OfficialHoliday("h7", "Sheetalashtami", "2026-03-11", "2026-03-11", "Wednesday", 1, "", "Festival"),
        OfficialHoliday("h8", "Eid-ul-Fitr", "2026-03-21", "2026-03-21", "Saturday", 1, "", "Religious"),
        OfficialHoliday("h9", "Ram Navami", "2026-03-26", "2026-03-26", "Thursday", 1, "", "Festival"),
        OfficialHoliday("h10", "Mahavir Jayanti", "2026-03-31", "2026-03-31", "Tuesday", 1, "", "Religious"),
        OfficialHoliday("h11", "Annual Closing of Commercial Banks", "2026-04-01", "2026-04-01", "Wednesday", 1, "", "Official"),
        OfficialHoliday("h12", "Good Friday", "2026-04-03", "2026-04-03", "Friday", 1, "", "Religious"),
        OfficialHoliday("h13", "Dr. B. R. Ambedkar Jayanti", "2026-04-14", "2026-04-14", "Tuesday", 1, "", "Gazetted"),
        OfficialHoliday("h14", "Buddha Purnima", "2026-05-01", "2026-05-01", "Friday", 1, "", "Gazetted"),
        OfficialHoliday("h15", "Mahavir Ji Fair (First Big Mela)", "2026-05-05", "2026-05-05", "Tuesday", 1, "", "Festival"),
        OfficialHoliday("h16", "Eid-ul-Adha (Bakrid)", "2026-05-27", "2026-05-27", "Wednesday", 1, "", "Religious"),
        OfficialHoliday("h17", "Muharram", "2026-06-26", "2026-06-26", "Friday", 1, "", "Religious"),
        OfficialHoliday("h18", "Independence Day", "2026-08-15", "2026-08-15", "Saturday", 1, "", "National"),
        OfficialHoliday("h19", "Eid-e-Milad / Barawafat", "2026-08-26", "2026-08-26", "Wednesday", 1, "", "Religious"),
        OfficialHoliday("h20", "Raksha Bandhan", "2026-08-28", "2026-08-28", "Friday", 1, "", "Festival"),
        OfficialHoliday("h21", "Janmashtami", "2026-09-04", "2026-09-04", "Friday", 1, "", "Festival"),
        OfficialHoliday("h22", "Mahatma Gandhi Jayanti", "2026-10-02", "2026-10-02", "Friday", 1, "", "National"),
        OfficialHoliday("h23", "Dussehra Mahanavami / Vijayadashami", "2026-10-20", "2026-10-20", "Tuesday", 1, "", "Festival"),
        OfficialHoliday("h24", "Diwali", "2026-11-08", "2026-11-08", "Sunday", 1, "", "Festival"),
        OfficialHoliday("h25", "Govardhan Puja", "2026-11-09", "2026-11-09", "Monday", 1, "", "Festival"),
        OfficialHoliday("h26", "Bhai Dooj / Chitragupt Jayanti", "2026-11-11", "2026-11-11", "Wednesday", 1, "", "Festival"),
        OfficialHoliday("h27", "Guru Nanak Jayanti / Kartik Purnima", "2026-11-24", "2026-11-24", "Tuesday", 1, "", "Gazetted"),
        OfficialHoliday("h28", "Christmas Day", "2026-12-25", "2026-12-25", "Friday", 1, "", "Gazetted")
    )
    val HOLIDAYS: List<AcademicHoliday> = OFFICIAL_HOLIDAYS_2026.map { h ->
        AcademicHoliday(h.startDate, h.name, h.notes)
    }

    fun getHolidayForDate(dateString: String): AcademicHoliday? {
        val match = OFFICIAL_HOLIDAYS_2026.find { h ->
            dateString >= h.startDate && dateString <= h.endDate
        }
        return if (match != null) AcademicHoliday(dateString, match.name, match.notes) else null
    }

    fun isHoliday(dateString: String): Boolean {
        return getHolidayForDate(dateString) != null
    }
}

