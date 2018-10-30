#SUPER SENDER
#By: Tyler McDowell
#Version: 0.1
#
#logs into email server and sends a pre developed email to an array of
#recipients, useful for sending out information to a group of students.

import smtplib
import datetime
import sys
import os
import re

def runSender():
    #SET UP
    HTMLmail = False

    emailList = ["email@test.edu"] #List of emails to send to

    SERVER = "smtp.gmail.com:587" #Email server

    FROM = "notMyEmail@test.edu" #server email address
    TO = emailList #make list

    USERNAME = "notMyEmail@test.edu" #login server details
    PASSWORD = "PASSWORD"

    text_subtype = 'plain'

    #GET INFO
    userCheck = False
    print("Current User: " + str(FROM))
    #PASSWORD = input("PASSWORD: ") #or hard code it
    userVar = input("Is this info correct (y/n)? ")
    if (userVar == 'y' or userVar == 'Y'):
        userCheck = True
    
    #GET EMAILS
        #TODO

    #GEN EMAIL CONTENT
    SUBJECT = "TEST..1..2..3"

    if (not HTMLmail):
        TEXT = "This is a test message to see what it looks like"
        
        message = """\
        From: %s
        To: %s
        Subjuct: %s

        %s
        """ % (FROM, ", ".join(TO), SUBJECT, TEXT)

    if (HTMLmail):
        message = """From: From Person <from@fromdomain.com>
        To: To Person <to@todomain.com>
        MIME-Version: 1.0
        Content-type: text/html
        Subject: SMTP HTML e-mail test

        This is an e-mail message to be sent in HTML format

        <b>This is HTML message.</b>
        <h1>This is headline.</h1>
        """
        
    if (userCheck):
        #CONNECT TO SERVER
        from smtplib import SMTP_SSL as SMTP
        from email.mime.text import MIMEText

        try:
            msg = MIMEText(message, text_subtype)
            msg['Subject']= SUBJECT
            msg['From']   = FROM # some SMTP servers will do this automatically, not all

            conn = smtplib.SMTP(SERVER)
            conn.starttls()
            conn.set_debuglevel(False)
            conn.login(USERNAME, PASSWORD)
            print("Successful Login")
            try:
                conn.sendmail(FROM, TO, msg.as_string())
                print("Successfully sent email to: " + str(TO))
            finally:
                conn.quit()
                print("ALL DONE!")
        except Exception:
            print("Error: unable to send email to: " + str(TO))
    else:
        print("Exit Successful")

runSender()
