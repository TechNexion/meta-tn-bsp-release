/*
 * @file nfc-i2c-tool.cpp
 *
 *
 * @Company Technexion Ltd.
 *
 * @author Wig Cheng
 * Contact: wig.cheng@technexion.com
 *
*/
#include "i2c-dev.h"
#include <iostream>
#include <string>
#include <fcntl.h>
#include <unistd.h>
#include <bitset>
#include <vector>

const uint8_t page_size_bytes = 16;

void read_nfc(int fd,uint8_t page)
{
  uint8_t values[16];
  uint8_t res  = i2c_smbus_read_i2c_block_data(fd, page, page_size_bytes, &values[0]);

  std::string ascii = "";

  for(int i=0;i<page_size_bytes;i++)
  {
    std::cout << "[" <<  i << "] -> 0x" << std::hex <<  (int)values[i] << std::endl;;
    if((int)values[i] >= 32 && (int)values[i] < 128)
    {
     ascii += values[i];
    }else
    {
     ascii += "_";
    }
  }

  std::cout << "ASCII= " << ascii << std::endl;
}

void write_nfc(int fd, uint8_t page, std::string s)
{
  std::vector<uint8_t> vals(s.begin(), s.begin()+page_size_bytes);
 
  for(int p=0;p<(16-vals.size());p++)
  {
    vals.push_back(' ');
  }

  uint8_t res = i2c_smbus_write_i2c_block_data(fd, page,page_size_bytes, &vals[0]);
  if(!res)
  {
    std::cout << "Write successful [Page=" << (int)page << "]" << std::endl;
  }else
  {
    std::cout << "WRITE ERROR" << std::endl;
  } 
}


int main(int argc, char* argv[])
{
  uint8_t page = 0x0; 
  int i2cDeviceFd = 0;
  int i2cAdapterNumber;
  uint8_t i2cSlaveAddress = 0x55; //default address;
  std::string i2cDevice;

  std::cout << "Reading NXP I2C-NFC tags" << std::endl;
  std::cout << "Usage: [app] i2c_adapter_number r page_number" << std::endl;
  std::cout << "Usage: [app] i2c_adapter_number w page_number string" << std::endl;
  std::cout << "----------------------------------------" << std::endl;

  if(*argv[1] == '0'){
    i2cAdapterNumber = 0;
  } else if(*argv[1] == '1'){
    i2cAdapterNumber = 1;
  } else if(*argv[1] == '2'){
    i2cAdapterNumber = 2;
  } else if(*argv[1] == '3'){
    i2cAdapterNumber = 3;
  } else if(*argv[1] == '4'){
    i2cAdapterNumber = 4;
  } else {
    std::cout << "Not support this i2c adapter number, please try again." << std::endl;
    return 0;
  }

  i2cDevice = "/dev/i2c-" + std::to_string(i2cAdapterNumber);
  i2cDeviceFd = open(i2cDevice.c_str(), O_RDWR);

  if(argc < 3)
  {
    return -1;
  }else
  {
      if (i2cDeviceFd < 0)
      {
       std::cout << "ERROR: file descriptor" << std::endl;
       return -1;
       }

       int ioCtlHandle = ioctl(i2cDeviceFd, I2C_SLAVE_FORCE, i2cSlaveAddress);

       if (ioCtlHandle < 0){
       std::cout << "ERROR: IOCTL" << std::endl;
       return -1;
       }

      if(*argv[2] == 'r'){
        uint8_t page = std::atoi(argv[3]);
        std::cout << "Reading 16 bytes [Slave=0x" << std::hex << (int)i2cSlaveAddress << ",Page=" << (int)page << "]" << std::endl;
        read_nfc(i2cDeviceFd,page);
      }

      if(*argv[2] == 'w'){
        uint8_t page = std::atoi(argv[3]);

      if(page == 0) {
          std::cout << "Page 0 was protected, please contact Wig to unlock it :)" << std::endl;
        } else {
          write_nfc(i2cDeviceFd,page,std::string(argv[4]));
        }
      }
   }

  return 0;
}
