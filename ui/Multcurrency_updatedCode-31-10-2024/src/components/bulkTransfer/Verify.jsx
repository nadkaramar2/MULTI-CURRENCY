import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { NavLink } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import CustomAlert from "../../layout/CustomAlert";
import MainPagination from "../../layout/MianPagination";
import AutoPagintation from "../../UI/AutoPagintation";
export default function Verify() {
  const [fetchdata, setFetchData] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);

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

  useEffect(() => {
    BulkTransferTransactions();
  }, []);

  const BulkTransferTransactions = async () => {
    try {
      const response = await amsApi.post(
        `bulkTransfer/getBulkTransferTransactions`,
        {},

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        if (response.data === "Bulk Transfer Not Found") {
          setFetchData([]);
          handleShowError("Bulk Transfer Not Found");
        } else {
          setFetchData(response.data);
          setCheckpoint(1);
        }
        // showSuccess(response.data.message);
      } else {
        handleShowError("Data Not Found");
      }
    } catch (error) {
      handleShowError("Data Not Found");
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Pending For Verification List
              </p>
              {/* <div className="bg-blue-100 px-4  flex items-center justify-end  sm:px-4"> */}
              <div className=" flex justify-center  rounded-md border border-transparent  px-2 mx-4 text-xs  font-medium text-white  ">
                <div className="pt-2 relative  mx-auto text-gray-600">
                  {" "}
                  <input
                    type="search"
                    id="search"
                    className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    title="Search Data"
                    type="submit"
                    className="absolute right-0 top-0 mt-4 mr-2"
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
                  {/* </div> */}
                </div>
              </div>
            </div>
          </div>
        </div>
        {/* <h1 className="font-medium flex bg-blue-300 justify-center  text-blue-800 text-2xl ">
          Pending For Verification List
        </h1> */}
        {/* <div className="flex justify-between items-center bg-blue-100 sm:px-6 lg:px-8  ">
          <h1 className="font-medium flex justify-center  text-black text-sx ">
            Pending For Verification List
          </h1>
        </div> */}
        {/* <div className="overflow-x-auto relative shadow-md sm:rounded-lg ml-16"> */}

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={fetchdata[0]?.strTotalCount}
          reCallApi={BulkTransferTransactions}
        />

        <div className="table-wrp block max-h-[27rem] ">
          <table className="w-full text-sm text-left text-black dark:text-blue-100">
            <thead className=" border-b text-xs sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
              <tr>
                <th scope="col" className="py-2.5 px-4 whitespace-nowrap">
                  Transaction ID
                </th>
                <th scope="col" className="py-2.5 px-4 whitespace-nowrap">
                  Date
                </th>
                <th scope="col" className="py-2.5 px-4 whitespace-nowrap">
                  Time
                </th>
                <th scope="col" className="py-2.5 px-4 whitespace-nowrap">
                  amount
                </th>
              </tr>
            </thead>

            <tbody>
              {fetchdata
                .filter((data) =>
                  data.strPreTransactionId
                    .toLowerCase()
                    .includes(searchQuery.toLowerCase())
                )
                .map(
                  ({
                    strPreTransactionId,
                    strBulkRequestDate,
                    strBulkRequestTime,
                    bulkTransferAmount,
                    strBulkMode,
                  }) => (
                    <tr
                      key={uuidv4()}
                      className=" border-b dark:border-neutral-500 "
                    >
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          <NavLink
                            title="Click on link "
                            to={`/verifylist/${strBulkMode}/${strPreTransactionId}`}
                            className="border-blue-300 shadow-md p-2  text-blue-700  rounded-full focus:ring-blue-400"
                          >
                            {strPreTransactionId || "-"}
                          </NavLink>
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {strBulkRequestDate || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {strBulkRequestTime || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 text-end whitespace-nowrap">
                        <div className="text-sm font-normal text-gray-900">
                          {bulkTransferAmount || "-"}
                        </div>
                      </td>
                    </tr>
                  )
                )}
            </tbody>
          </table>
        </div>

        {/* </div> */}
        {/* </div> */}
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}

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
