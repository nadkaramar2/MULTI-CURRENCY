import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { XMarkIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
export default function Customer_Search() {
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const [checked, setChecked] = useState(true);
  const [alldata, setAlldata] = useState([]);
  const [custid, setCustid] = useState("");
  localStorage.setItem("custid", custid);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (custid === "" || custid.length === 0) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `customerId/getCustmerInfo`,

          {
            strCustId: custid,
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.customerIdCreation);

          let firstname = response.data.customerIdCreation.strFirstName;
          localStorage.setItem("Firstname", firstname);

          let strLastName = response.data.customerIdCreation.strLastName;
          localStorage.setItem("LastName", strLastName);
          // showSuccess(response.data.message);
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setAlldata([]);
          setChecked(true);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
        setAlldata([]);
        setChecked(true);
      }
    }
  };

  const handleClick = () => {
    setCustid("");
    setAlldata([]);
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full ">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg  ">
            <div className=" flex items-center justify-between ">
              <div className=" flex  items-center justify-between">
                <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                  Search Customer
                </p>
              </div>
              <div>
                {/* <div className="max-w-full mx-auto h-full ">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-8 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between ">
              <p className="text-sm  sm:text-xm  text-black :text-2xl  leading-normal ">
                  Search Customer
              </p>
            </div>
          </div>
        </div> */}
                <button className="inline-flex sm:xm-3  sm:mt-0 items-start justify-end px-8  focus:outline-none rounded text-xs font-normal">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1 bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-1 sm:p-2 bg-white">
                <div className="grid gap-1 md:grid-cols-4 px-8">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block  text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Customer id <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="custid"
                      value={custid}
                      // onChange={(e) => setCustid(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setCustid(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (custid.length === 0) {
                          setError("Please enter Customer id!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-6 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cust Id"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && custid.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please enter Customer id!
                      </p>
                    ) : null}
                    {/* {error && custid.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please enter Customer id
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                  <div className="my-4">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                    >
                      <MagnifyingGlassIcon className="h-3 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2  font-bold  text-white  border border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-3 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div className="overflow-x-auto relative shadow-md sm:rounded-lg mt-1 ">
        <div className="table-wrp block max-h-[27rem]  max-w-[27rem]   ">
          <table className="w-full text-xs text-left text-black dark:text-blue-100">
            <thead className="text-xs border-b sticky right-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
              <th scope="col" className="py-2.5 px-6  whitespace-nowrap">
                First Name
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Middle Name
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Last Name
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Gender
              </th>

              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Date of Birth
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Email ID
              </th>

              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Address1
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Address2
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Address3
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                Country
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                State
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                City
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                Mobile No
              </th>
              <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                Postal Code
              </th>
            </thead>
            <tbody>
              {alldata.length === 0 ? (
                <tr>
                  <td colSpan="12" className="px-6 py-1.5 text-center">
                    <span className="text-sm font-normal text-gray-500">
                      No data available.
                    </span>
                  </td>
                </tr>
              ) : (
                <tr key={uuidv4()} className="border-b dark:border-neutral-500">
                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strFirstName || "-"}
                    </span>
                  </td>
                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strMiddleName || "-"}
                    </span>
                  </td>
                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strLastName || "-"}
                    </span>
                  </td>
                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strGender || "-"}
                    </span>
                  </td>

                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.birthDate || "-"}
                    </span>
                  </td>
                  <td className="whitespace-nowrap px-6 py-2.5">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strEmailID || "-"}
                    </span>
                  </td>

                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strAddress1 || "-"}
                    </span>
                  </td>
                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.address2 || "-"}
                    </span>
                  </td>
                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.address3 || "-"}
                    </span>
                  </td>

                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strCountry || "-"}
                    </span>
                  </td>
                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strState || "-"}
                    </span>
                  </td>
                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strCity || "-"}
                    </span>
                  </td>

                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strMobileNo || "-"}
                    </span>
                  </td>
                  <td className="px-6 py-2.5 whitespace-nowrap">
                    <span className="text-sm font-medium text-gray-900">
                      {alldata.strPinCode || "-"}
                    </span>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
