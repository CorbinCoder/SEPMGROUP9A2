Source code available on [Github](https://github.com/CorbinCoder/SEPMGROUP9A2)

## Steps to run the Application
1. Unzip the java code.
2. Open cmd and cd into the directory SEPMGROUP9A2
3. Compile the code with the command "javac -cp CiscoTicketing/src CiscoTicketing/src/Main/*.java"
4. Run the code with the command "java -cp CiscoTicketing/src Main.Main"

## Default Technician Details
| Name            | E-Mail                         | Phone      | Password                | Level |
| --------------- | ------------------------------ | ---------- | ----------------------- | ----- |
| Harry Styles    | harry.styles@team.cisco.com    | 0425363455 | `P:"/%"\)1pgi*{tCe#0.` | One   |
| Niall Horan     | niall.horan@team.cisco.com     | 0425411004 | `7O7&2dYH4PbogT?_P&<Hp` | One   |
| Liam Payne      | liam.payne@team.cisco.com      | 0425002266 | `,dP"_sUQ\EklO(0s+37c`  | One   |
| Louis Tomlinson | louis.tomlinson@team.cisco.com | 0425789123 | `QyDgn2a{1r&5"F}LIXX&`  | Two   |
| Zayn Malik      | zayn.malik@team.cisco.com      | 0425121333 | `W'oA%m%QV#TtYlc*Q_9_`  | Two   |


## Test Data

The `testData()` method in the `TicketingSystem` class allows you to populate the system with sample data for testing and demonstration purposes. It adds a set of predefined staff members and generates a specified number of test tickets.

To add the test data, uncomment the following line in the `Main.java` file:
```java
ticketingSystem.testData(20);
```
The argument passed to `testData()` (20 in this example) determines the number of test tickets to be generated.

### Test Staff Member Details

The `testData()` method adds the following predefined staff members to the system:

| Name            | E-Mail                         | Phone     | Password                |
|----------------|--------------------------------|------------|-------------------------|
| Patricia Smith | patricia.smith@demo.com        | 0776172603 | `oyoxpWn=]x?}ImNqMpsH`  |
| Robert Miller  | robert.miller@sample.com       | 0185074039 | `` `ndHx~'s>]0tiTrp5c$y ``  |
| James Miller   | james.miller@example.com       | 0276776872 | `&Fkq.;/}tw<Iqu@jd+@I`  |
| Michael Johnson| michael.johnson@example.com    | 0102241558 | `ia""q(]O&v[rly/O0mhh`  |
| Robert Garcia  | robert.garcia@test.com         | 0361294323 | `3K1Re7,$q_9&&*b\O{^E` |
| Robert Johnson | robert.johnson@test.com        | 0508417220 | `$\?SH8/T;[Q_al3Wyb1L` |
| Patricia Brown | patricia.brown@example.com     | 0191952420 | `nl<M]n]+v:9LC+E&(~~t`  |
| Robert Garcia  | robert.garcia@sample.com       | 0720165987 | `@J)[.4?vy5W}hE{J~K!r`  |
| Michael Williams| michael.williams@myapp.com    | 0676046904 | `#=]f[v{FYxkh-pcrFzl4`  |
| Jennifer Davis | jennifer.davis@example.com     | 0621067644 | `jM<6sO]0q&W6dd$k^4^A`  |

### Test Tickets

The `testData()` method generates a specified number of test tickets, determined by the argument passed to it. Each generated ticket has the following characteristics:
- Random creation time: The creation time of each ticket is randomly set within the past 24 hours from the current time.
- Random severity: The severity of each ticket is randomly assigned as either LOW, MEDIUM, or HIGH.
- Random status: The status of each ticket is randomly set as either OPEN, CLOSED_AND_RESOLVED, or CLOSED_AND_UNRESOLVED.

The test tickets are automatically assigned to random staff members from the predefined list.

