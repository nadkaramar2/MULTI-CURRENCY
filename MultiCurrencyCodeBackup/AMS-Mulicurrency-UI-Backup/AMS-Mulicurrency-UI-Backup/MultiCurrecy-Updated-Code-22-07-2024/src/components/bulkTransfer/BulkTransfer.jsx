import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";

function BulkTransfer() {
  const [bulkmode, setBulkmode] = useState("");
  const [imgfile, setImgFile] = useState(null);
  const [data, setData] = useState([]);
  const [error, setError] = useState("");
  let useid = localStorage.getItem("userName");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

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

  const handleFileUpload = async (event) => {
    event.preventDefault();

    if (event.target.files[0] === undefined) {
      setError("file not selected");

      return false;
    } else {
      setImgFile(event.target.files[0]);
    }
  };

  const photoupload = async (e) => {
    e.preventDefault();

    if (imgfile === null || bulkmode === "") {
      swal("Please select both Bulk Mode and an image file");
    } else {
      try {
        const formData = new FormData();
        formData.append("file", imgfile);
        formData.append("strBulkMode", bulkmode);
        // formData.append("strMakerId", useid);

        const response = await amsApi.post(
          `bulkTransfer/uploadExcel`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.data.code === "S0000") {
          setData(response.data.bulkTransferExcelReqRes);
          handleShowSuccess(response.data.message);
          setCheckpoint(1);
          setError("");
          setImgFile(null); // Set imgfile to null to indicate no file is selected
          setBulkmode("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  // submit API
  const postData = async () => {
    const bulkmode = data;
    if (bulkmode === "" || bulkmode.length === 0) {
      swal("List of BulkTransfer Excel Data not found ");
    } else if (!useid) {
      swal("Please check your userId ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/saveBulkTransferExcelData`,
          {
            strListBulkTransferExcelReqRes: bulkmode,
            strMakerId: useid,
          },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full ">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xlm text-black :text-2xl  leading-normal ">
                Upload
              </p>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-8  bg-white  ">
                <div className="grid gap-3  md:grid-cols-3 px-8  ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Bulk Mode <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="bulkmode"
                      name="bulkmode"
                      value={bulkmode}
                      onChange={(e) => setBulkmode(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="OTM">OTM</option>
                      <option value="MTO">MTO</option>
                      <option value="GTM">GTM</option>
                      <option value="MTG">MTG</option>
                    </select>
                    {error && bulkmode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Bulk Mode !
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Upload Excel <span className="text-red-600 px-2">*</span>
                    </label>

                    <input
                      id="file-upload"
                      type="file"
                      name="file_upload"
                      onChange={handleFileUpload}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />

                    {/* <img src={file} className=" h-40 mt-2 " /> */}
                  </div>
                </div>
              </div>

              <div className="flex justify-end px-4 py-1 bg-gray-100 mb-1 ">
                <button
                  title="Upload File"
                  type="button"
                  onClick={photoupload}
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-blue-700 rounded"
                >
                  Upload a file
                </button>
              </div>
            </div>
          </form>
        </div>

        {/* table */}
        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100 mt-1 rounded-t-lg ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-xs  font-medium  focus:outline-none focus:ring-2 py-1 ">
            <div className="pt-2 relative mx-auto text-gray-600">
              <input
                type="search"
                id="search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                }}
                disabled={checkpoint !== 1}
                className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
              />
              <button
                title="Search Here"
                type="submit"
                className="absolute right-0 top-0 mt-3  px-2  "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
        <div className="overflow-x-auto relative shadow-md sm:rounded-lg mt-1 ">
          {/* {filteredData.length > 0 ? ( */}
          <>
            <div className="table-wrp block max-h-[27rem] max-w-[21rem]  ">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className="text-xs border-b sticky right-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  {/* <tr> */}
                  <th scope="col" className="py-2.5 px-16 whitespace-nowrap">
                    from Account type
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    From Account no
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    from Account name
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    to Account type
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    To Account no
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    To Account name
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    Transaction amount
                  </th>
                  {/* </tr> */}
                </thead>

                <tbody>
                  {data.length > 0 ? (
                    <>
                      {data?.map((data) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strFromAccountType || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strFromAccountNo || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strFromAccountName || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strToAccountType || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strToAccountNo || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strToAccountName || ""}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-xsclassName text-gray-900">
                              {data.strAmount || ""}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </>
                  ) : (
                    <td colSpan="6" className="px-6 py-1 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  )}
                </tbody>
              </table>
            </div>
          </>
          {/* )} */}
        </div>
      </div>
      <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
        <button
          title="Click Submit Button"
          type="button"
          onClick={postData}
          data-modal-toggle="defaultModal"
          className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
        >
          Submit
        </button>
        <button
          type="submit"
          title="Clear Data"
          data-modal-toggle="defaultModal"
          className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
        >
          Clear
        </button>
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
    </AppLayout>
  );
}

export default BulkTransfer;
