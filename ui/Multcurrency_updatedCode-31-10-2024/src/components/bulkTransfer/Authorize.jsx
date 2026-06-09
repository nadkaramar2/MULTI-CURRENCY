import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { NavLink } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function Authorize() {
  const [fetchdata, setFetchData] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  let useid = localStorage.getItem("userName");

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
    if (!useid) {
      swal("Please check your userId ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/authorizeBulklist`,
          { strMakerId: useid },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.status === 200) {
          if (response.data === "Bulk Transfer Not Found") {
            setFetchData([]);
          } else {
            setFetchData(response.data);
            setCheckpoint(1);
          }
        } else {
          handleShowError("Bulk Transfer Not Found");
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  // sorted table data
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const sortedData = fetchdata.slice(0).sort((a, b) => {
    const dateA = new Date(a.strBulkRequestDate);
    const dateB = new Date(b.strBulkRequestDate);
    const timeA = new Date("1970/01/01 " + a.strBulkRequestTime);
    const timeB = new Date("1970/01/01 " + b.strBulkRequestTime);

    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Authorize Bulk Transfers
              </p>
              <div className="flex justify-end rounded-md border border-transparent px-2 mx-2 text-xs font-medium text-white ">
                <div className="pt-2 relative mx-auto text-gray-600">
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
                    className="absolute right-0 top-0 mt-2 mr-2"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 16"
                      strokeWidth={1}
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
          </div>
        </div>
        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={fetchdata[0]?.strTotalCount}
          reCallApi={BulkTransferTransactions}
        />

        <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[27rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-2.5 px-6"
                  onClick={() => handleSort("strPreTransactionId")}
                >
                  Transaction id {renderSortArrow("strPreTransactionId")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-6"
                  onClick={() => handleSort("strBulkRequestDate")}
                >
                  Txn date {renderSortArrow("strBulkRequestDate")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-6"
                  onClick={() => handleSort("strBulkRequestTime")}
                >
                  txn time {renderSortArrow("strBulkRequestTime")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-6"
                  onClick={() => handleSort("strAmount")}
                >
                  Amount {renderSortArrow("strAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-6"
                  onClick={() => handleSort("strMakerId")}
                >
                  Maker userid {renderSortArrow("strMakerId")}
                </th>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData
                      .filter(({ strPreTransactionId }) =>
                        strPreTransactionId
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase())
                      )
                      .map(
                        ({
                          strPreTransactionId,
                          strBulkRequestDate,
                          strBulkRequestTime,
                          strAmount,
                          strBulkMode,
                          strMakerId,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className=" border-b dark:border-neutral-500"
                          >
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                <NavLink
                                  title="Click on link"
                                  to={`/authorizeapprove/${strBulkMode}/${strPreTransactionId}`}
                                  className="border-blue-300 shadow-md p-2  text-blue-700   rounded-full focus:ring-blue-400"
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
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {strAmount || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {strMakerId || "-"}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
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
        </div>
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
