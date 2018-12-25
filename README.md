# Ext IP daemon

Ext IP daemon is API that runs on your PC or any device that can run Java and monitors public IP address. It's usefull when you don't have static public IP address.

Daemon runs in console/terminal and doesent have GUI. 

Daemon is using http://checkip.amazonaws.com to get public ip address.

## Usage

Just run jar file with `java -jar <filename>` and make sure to have active internet connection.

## Configuration

When you run daemon for the first time app will ask you for your email information. All information you enter is stored only localy and in plain text.

If you choose to create congifuration file manually file must be named `extipdaemon.cfg` and must have following data:

```
smtpauth=SMTP authentication (true/false)
password=Your email account password
starttls=Set STARTTLS enabled (true/false)
smtpsrv=Your email SMTP server address
receiverAddress=email address of the reciever
smtpport=Your email SMTP server port (25, 465 or 587)
username=Your email account username
```

Exapmle:

```
smtpauth=true
password=123456789
starttls=true
smtpsrv=mail.example.com
receiverAddress=receiver@example.com
smtpport=465
username=sender@example.com
```

Save configuration file in the same location as jar file.

## Problems

If you are experiencing any problems delete `extipdaemon.cfg` file and re-run app.

## License
[GPL](https://www.gnu.org/licenses/gpl-3.0.en.html)
