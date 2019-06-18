#include <plib.h>
#include "TCPIPConfig.h"
//#include "temperature.h"
//#include <stdio.h>
#include <HardwareProfile.h>
//#include "ConfigurationBits.h"
#include "temperature.h"

#if defined(STACK_USE_TCP_TO_UPPER_SERVER)

#include "TCPIP Stack/TCPIP.h"

//#include <ctype.h>

// Defines which port the server will listen on
#define TCP_TO_UPPER_SERVER_PORT    7777

//void extern temperature();

static enum _myState {
    SM_OPEN_SERVER_SOCKET = 0,
    SM_LISTEN_FOR_CLIENT_CONNECTION,
    SM_PROCESS,
    SM_DISCONNECT_CLIENT
} myState = SM_OPEN_SERVER_SOCKET;

static enum _commandEnums {
    DO_NO_COMMAND = 0,
    DO_QUIT,
    DO_TO_UPPER,
} myCommand = DO_NO_COMMAND;

/*****************************************************************************
  Function:
        void TCP_To_Upper_Server(void)

  Summary:
        Implements a simple TCP Server, which inputs a character form a client, converts the received character to upper case, and sends it back to the client.

  Description:
        Implements a simple TCP Server, which inputs a character form a client, converts the received character to upper case, and sends it back to the client.
	
        This example can be used as a model for many TCP server applications.

  Precondition:
        TCP is initialized.

  Parameters:
        None

  Returns:
        None
 ***************************************************************************/

void changeLed(TCP_SOCKET mySocket);


// ****************************************************************************
// ****************************************************************************
// Local Support Routines
// ****************************************************************************
// ****************************************************************************

/*******************************************************************************
  Function:
    BOOL BeginTransfer( BOOL restart )

  Summary:
    Starts (or restarts) a transfer to/from the I2C Slave.

  Description:
    This routine starts (or restarts) a transfer to/from the I2C Slave, waiting (in
    a blocking loop) until the start (or re-start) condition has completed.

  Precondition:
    The I2C module must have been initialized.

  Parameters:
    restart - If FALSE, send a "Start" condition
            - If TRUE, send a "Restart" condition
    
  Returns:
    TRUE    - If successful
    FALSE   - If a collision occured during Start signaling
    
  Example:
    <code>
    BeginTransfer(USING_START_METHOD);
    </code>

  Remarks:
    This is a blocking routine that waits for the bus to be idle and the Start
    (or Restart) signal to complete.
 *****************************************************************************/

BOOL BeginTransfer(BOOL restart) {
    I2C_STATUS status;

    // Send the Start (or Restart) signal
    if (restart) {
        I2CRepeatStart(TMP2_I2C_BUS);
    } else {
        // Wait for the bus to be idle, then start the transfer
        while (!I2CBusIsIdle(TMP2_I2C_BUS));

        if (I2CStart(TMP2_I2C_BUS) != I2C_SUCCESS) {
            DBPRINTF("Error: Bus collision during transfer Start\n");
            return FALSE;
        }
    }

    // Wait for the signal to complete
    do {
        status = I2CGetStatus(TMP2_I2C_BUS);

    } while (!(status & I2C_START));

    return TRUE;
}

/*******************************************************************************
  Function:
    BOOL TransmitOneByte( UINT8 data )

  Summary:
    This transmits one byte to the I2C Slave.

  Description:
    This transmits one byte to the I2C Slave, and reports errors for any bus
    collisions.

  Precondition:
    The transfer must have been previously started.

  Parameters:
    data    - Data byte to transmit

  Returns:
    TRUE    - Data was sent successfully
    FALSE   - A bus collision occured

  Example:
    <code>
    TransmitOneByte(0xAA);
    </code>

  Remarks:
    This is a blocking routine that waits for the transmission to complete.
 *****************************************************************************/

BOOL TransmitOneByte(UINT8 data) {
    // Wait for the transmitter to be ready
    while (!I2CTransmitterIsReady(TMP2_I2C_BUS));

    // Transmit the byte
    if (I2CSendByte(TMP2_I2C_BUS, data) == I2C_MASTER_BUS_COLLISION) {
        DBPRINTF("Error: I2C Master Bus Collision\n");
        return FALSE;
    }

    // Wait for the transmission to finish
    while (!I2CTransmissionHasCompleted(TMP2_I2C_BUS));

    return TRUE;
}

/*******************************************************************************
  Function:
    void StopTransfer( void )

  Summary:
    Stops a transfer to/from the I2C Slave.

  Description:
    This routine Stops a transfer to/from the I2C Slave, waiting (in a
    blocking loop) until the Stop condition has completed.

  Precondition:
    The I2C module must have been initialized & a transfer started.

  Parameters:
    None.
    
  Returns:
    None.
    
  Example:
    <code>
    StopTransfer();
    </code>

  Remarks:
    This is a blocking routine that waits for the Stop signal to complete.
 *****************************************************************************/

void StopTransfer(void) {
    I2C_STATUS status;

    // Send the Stop signal
    I2CStop(TMP2_I2C_BUS);

    // Wait for the signal to complete
    do {
        status = I2CGetStatus(TMP2_I2C_BUS);

    } while (!(status & I2C_STOP));
}

void Acknowledge(BOOL ack) {
    I2CAcknowledgeByte(TMP2_I2C_BUS, ack);
    while (I2CAcknowledgeHasCompleted(TMP2_I2C_BUS) == FALSE);
}



float temperature() {
    float temp;
    UINT8 i, dummy;
    UINT32 actualClock;

    typedef union {

        struct {
            char LB;
            char HB;
        } bytes;
        short int _16bitValue;
    } TMP2;
    TMP2 myTemp;

    // Set the I2C baudrate
    actualClock = I2CSetFrequency(TMP2_I2C_BUS, GetPeripheralClock(), I2C_CLOCK_FREQ);
    if (abs(actualClock - I2C_CLOCK_FREQ) > I2C_CLOCK_FREQ / 10) {
        DBPRINTF("Error: TMP2_I2C_BUS clock frequency (%u) error exceeds 10%%.\n", (unsigned) actualClock);
    }

    // Enable the I2C bus
    I2CEnable(TMP2_I2C_BUS, TRUE);

        while (BeginTransfer(USING_START_METHOD) == FALSE); //Keep on trying to set the start condition
        TransmitOneByte(TMP2_ADDR_WRITE); //device address with WRITE MODE
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        TransmitOneByte(TMP2_DEV_ID_REG_ADDR); //select configuration register
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        while (BeginTransfer(USING_RESTART_METHOD) == FALSE); //Keep on trying to set the restart condition
        TransmitOneByte(TMP2_ADDR_READ); //device address with READ MODE
        while (I2CReceiverEnable(TMP2_I2C_BUS, TRUE) == I2C_RECEIVE_OVERFLOW);
        while (!I2CReceivedDataIsAvailable(TMP2_I2C_BUS));
        Acknowledge(NACK);
        while ((dummy = I2CGetByte(TMP2_I2C_BUS) != TMP2_DEV_ID));
        StopTransfer();

    while (BeginTransfer(USING_START_METHOD) == FALSE); //Keep on trying to set the start condition
    TransmitOneByte(TMP2_ADDR_WRITE); //device address with WRITE MODE
    while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
    TransmitOneByte(CONFIG_REG); //select configuration register
    while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
    TransmitOneByte(0b10000000); //set configuration register to 16-bit mode, all others default
    while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
    StopTransfer();

        while (BeginTransfer(USING_START_METHOD) == FALSE); //Keep on trying to set the start condition
        TransmitOneByte(TMP2_ADDR_WRITE); //device address with WRITE MODE
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        TransmitOneByte(CONFIG_REG); //select configuration register
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        while (BeginTransfer(USING_RESTART_METHOD) == FALSE); //Keep on trying to set the restart condition
        TransmitOneByte(TMP2_ADDR_READ); //device address with READ MODE
        while (I2CReceiverEnable(TMP2_I2C_BUS, TRUE) == I2C_RECEIVE_OVERFLOW);
        while (!I2CReceivedDataIsAvailable(TMP2_I2C_BUS));
        Acknowledge(NACK);
        while ((dummy = I2CGetByte(TMP2_I2C_BUS) != 0b10000000));
        StopTransfer();

        while (BeginTransfer(USING_START_METHOD) == FALSE); //Keep on trying to set the start condition
        TransmitOneByte(TMP2_ADDR_WRITE); //device address with WRITE MODE
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        TransmitOneByte(TEMP_REG_MSB); //select configuration register
        while (I2CByteWasAcknowledged(TMP2_I2C_BUS) == FALSE);
        while (BeginTransfer(USING_RESTART_METHOD) == FALSE); //Keep on trying to set the restart condition
        TransmitOneByte(TMP2_ADDR_READ); //device address with READ MODE
        while (I2CReceiverEnable(TMP2_I2C_BUS, TRUE) == I2C_RECEIVE_OVERFLOW);
        while (!I2CReceivedDataIsAvailable(TMP2_I2C_BUS));
        Acknowledge(ACK);
        myTemp.bytes.HB = I2CGetByte(TMP2_I2C_BUS);
        while (I2CReceiverEnable(TMP2_I2C_BUS, TRUE) == I2C_RECEIVE_OVERFLOW);
        while (!I2CReceivedDataIsAvailable(TMP2_I2C_BUS));
        Acknowledge(NACK);
        myTemp.bytes.LB = I2CGetByte(TMP2_I2C_BUS);
        temp = (float) myTemp._16bitValue*TMP2_RESOLUTION;
        StopTransfer();

        return temp;
}
static char command[10];
static int commandIndex = 0;



int compare(char* c1, char* c2)
{
    int sum = 0;
    int i = 0;
    while (c1[i] != 0 && c2[i] != 0)
    {
        if (c1[i] != c2[i])
            sum++;
        i++;
    }
    
    return sum;
}

void TCPToUpperServer(void) {
    static TCP_SOCKET mySocket;
    WORD numBytes = 0;
    char theChar;
    
    
    switch (myState) {
        case SM_OPEN_SERVER_SOCKET:
            mySocket = TCPOpen(0, TCP_OPEN_SERVER, TCP_TO_UPPER_SERVER_PORT, TCP_PURPOSE_TCP_TO_UPPER_SERVER);
            if (mySocket == INVALID_SOCKET)
                return;
            myState = SM_LISTEN_FOR_CLIENT_CONNECTION;
            break;

        case SM_LISTEN_FOR_CLIENT_CONNECTION:
            if (TCPIsConnected(mySocket) == FALSE)
                return;
            else {
                myState = SM_PROCESS;
                break;
            }
            
        case SM_PROCESS:
            if (TCPIsConnected(mySocket) == FALSE) {
                myState = SM_DISCONNECT_CLIENT;
                return;
            }
            if (TCPIsPutReady(mySocket) < (WORD) 1)
                return;
            
            if ((numBytes = TCPIsGetReady(mySocket)) == 0)
                myCommand = DO_NO_COMMAND;
            else
            {
                TCPGet(mySocket, &theChar);
                if (theChar != (char)0xff)
                {
                    command[commandIndex] = theChar;
                    commandIndex++;
                }
                else
                {
                    command[commandIndex] = 0;
                    commandIndex = 0;
                    changeLed(mySocket);
                    
                }
            }
            break;
            
        case SM_DISCONNECT_CLIENT:
            TCPDisconnect(mySocket);
            myState = SM_LISTEN_FOR_CLIENT_CONNECTION;
            break;
    }
}

void changeLed( TCP_SOCKET mySocket){

    int size_1 = sizeof("LED 12 Off ");
    
    if(compare(command, "temp") == 0){
        float temp = temperature();
        char temperature[10];
        //TCPPutArray(mySocket, (BYTE*)(""),size_1);
        sprintf(temperature,"Temp: %f",temp);
        TCPPutArray(mySocket, (BYTE*)(temperature),sizeof(temperature));
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else 
    if(compare(command, "L1on") == 0){
        LED0_IO = 1;
        TCPPutArray(mySocket, (BYTE*)"LED 1 On ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L1off") == 0){
        LED0_IO = 0;
        TCPPutArray(mySocket, (BYTE*)"LED 1 Off ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L2on") == 0){
        LED1_IO = 1;
        TCPPutArray(mySocket, (BYTE*)"LED 2 On ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L2off") == 0){
        LED1_IO = 0;
        TCPPutArray(mySocket, (BYTE*)"LED 2 Off ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L3on") == 0){
        LED2_IO = 1;
        TCPPutArray(mySocket, (BYTE*)"LED 3 On ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L3off") == 0){
        LED2_IO = 0;
        TCPPutArray(mySocket, (BYTE*)"LED 3 Off ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L4on") == 0){
        LED3_IO = 1;
        TCPPutArray(mySocket, (BYTE*)"LED 4 On ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "L4off") == 0){
        LED3_IO = 0;
        TCPPutArray(mySocket, (BYTE*)"LED 4 Off ",size_1);
        TCPPut(mySocket, (BYTE)0xff);
        TCPFlush(mySocket);
    }
    else if(compare(command, "gpb1") == 0){
        if (BUTTON0_IO == 1)
        {
            TCPPutArray(mySocket, (BYTE*)"PB 1 Down ",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
        else
        {
            TCPPutArray(mySocket, (BYTE*)"PB 1 Up",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
    }
    else if(compare(command, "gpb2") == 0){
        if (BUTTON1_IO == 1)
        {
            TCPPutArray(mySocket, (BYTE*)"PB 2 Down ",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
        else
        {
            TCPPutArray(mySocket, (BYTE*)"PB 2 Up",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
    }
    else if(compare(command, "gpb3") == 0){
        if (BUTTON2_IO == 1)
        {
            TCPPutArray(mySocket, (BYTE*)"PB 3 Down ",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
        else
        {
            TCPPutArray(mySocket, (BYTE*)"PB 3 Up ",size_1);
            TCPPut(mySocket, (BYTE)0xff);
            TCPFlush(mySocket);
        }
    }
}

#endif //#if defined(STACK_USE_TCP_TO_UPPER_SERVER)